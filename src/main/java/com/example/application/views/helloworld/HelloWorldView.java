package com.example.application.views.helloworld;

import java.util.ArrayList;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Hello World")
@Route(value = "hello")
@RouteAlias(value = "")
@Uses(Icon.class)
public class HelloWorldView extends Div {

	private ArrayList<Row> parents = new ArrayList<>();
	
	static class Row {
		Row parent;
		String entry;
		
		ArrayList<Row> children;

		public Row(Row parent, String entry) {
			super();
			this.parent = parent;
			this.entry = entry;
			
			if(parent!=null) {
				if(parent.children==null) {
					parent.children = new ArrayList<>();
					parent.children.add(this);
				}
			}
		}

		public Row getParent() {
			return parent;
		}

		public String getEntry() {
			return entry;
		}
		
		public boolean matches(String filterText) {
			if(entry.startsWith(filterText)) {
				return true;
			}
			if(children!=null) {
				for(Row child : children) {
					if(child.matches(filterText)) {
						return true;
					}
				}
			}
			return false;
		}
		
		
	}
	
    public HelloWorldView() {
    	
    	// Build some data
    	
    	var treeData = new TreeData<Row>();
    	
    	for(int parentIdx = 0; parentIdx<10; parentIdx++) {
    	
    		Row parentRow = new Row(null, "Parent " + parentIdx);
    		treeData.addItem(null, parentRow);
    		parents.add(parentRow);

    		for(int childIdx = 0; childIdx<10; childIdx++) {
    			treeData.addItem(parentRow, new Row(parentRow, "Child " + childIdx));
    		}
    	
    	}
    	
    	var treeDataProvider = new TreeDataProvider<Row>(treeData);
    	
    	//
    	
    	var clearThenCollapseCheckbox = new Checkbox("Clear then collapse");
    	
    	var filterField = new TextField();
    	filterField.setValueChangeMode(ValueChangeMode.LAZY);
    	
    	var treeGrid = new TreeGrid<Row>(treeDataProvider);
    	treeGrid.addHierarchyColumn(Row::getEntry);
    	
    	filterField.addValueChangeListener(event -> {
    		
    		String filterText = filterField.getValue();
    		boolean clearThenCollapse = clearThenCollapseCheckbox.getValue();
    		
            if(filterText.isEmpty()) {
            	if(clearThenCollapse) {
	            	treeDataProvider.clearFilters();
	                treeGrid.collapse(parents);
            	}
            	else {
            		treeGrid.collapse(parents);
	            	treeDataProvider.clearFilters();
            	}
            }
            else {
                treeGrid.expand(parents);
            }
            treeDataProvider.addFilter(row -> row.matches(filterText));
    		
    	});
    	
    	this.setSizeFull();
    	filterField.setWidthFull();
    	treeGrid.setSizeFull();
    	add(clearThenCollapseCheckbox, filterField, treeGrid);

    }

}
