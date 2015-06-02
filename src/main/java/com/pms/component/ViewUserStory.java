package com.pms.component;

import com.pms.DashboardUI;
import com.pms.dao.UserStoryDAO;
import com.pms.domain.Task;
import com.pms.domain.UserStory;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by Damitha on 6/2/2015.
 */
public class ViewUserStory extends CustomComponent {

    public VerticalLayout viewUserStoryLayout;
    private String projectName;
    private String userStoryName;
    private Button create;
    private Table tasksTable;
    private UserStory userStory;

    public ViewUserStory(String projectName,String userStoryName)
    {
        this.projectName=projectName;
        this.userStoryName=userStoryName;
        buildViewUserStory();

    }


    public Component getUserStory()
    {
        return viewUserStoryLayout;

    }


    private void buildViewUserStory()
    {
        viewUserStoryLayout = new VerticalLayout();
        //viewUserStoryLayout.setCaption("View Project");
        viewUserStoryLayout.setMargin(true);
        viewUserStoryLayout.setSpacing(true);



        UserStoryDAO userStoryDAO=(UserStoryDAO) DashboardUI.context.getBean("UserStory");
        userStory= userStoryDAO.getUserStoryFormProjectNameAndUserStoryName(projectName,userStoryName);

        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label title = new Label(userStory.getProject().getName()+" - "+userStory.getName());
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        viewUserStoryLayout.addComponent(header);

        if(userStory!=null)
        {
            Label name = new Label("User Story Name :   "+userStory.getName());
            viewUserStoryLayout.addComponent(name);
            Label description= new Label("Description :   "+userStory.getDescription());
            viewUserStoryLayout.addComponent(description);
            Label priority= new Label("Priority :   "+userStory.getPriority());
            viewUserStoryLayout.addComponent(priority);
            Label date= new Label("Date :   "+userStory.getDate());
            viewUserStoryLayout.addComponent(date);
            Label preRequisits= new Label("Pre Requisits :   "+userStory.getPreRequisits());
            viewUserStoryLayout.addComponent(preRequisits);
            Label dependancy= new Label("Dependancy :   "+userStory.getDependancy());
            viewUserStoryLayout.addComponent(dependancy);
            Label domain= new Label("Domain :   "+userStory.getDomain());
            viewUserStoryLayout.addComponent(domain);
            Label assignedSprint= new Label("Assigned Sprint :   "+userStory.getAssignedSprint());
            viewUserStoryLayout.addComponent(assignedSprint);
            Label releasedDate= new Label("Released Date :   "+userStory.getReleasedDate());
            viewUserStoryLayout.addComponent(releasedDate);



            viewUserStoryLayout.addComponent(buildToolbar());


            tasksTable= new Table("");
            tasksTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
            tasksTable.addStyleName(ValoTheme.TABLE_COMPACT);
            tasksTable.setSelectable(true);

            tasksTable.addContainerProperty("Index", Integer.class, null);
            tasksTable.addContainerProperty("Name",  String.class, null);
            tasksTable.addContainerProperty("Priority", Integer.class, null);
            tasksTable.addContainerProperty("Severity", Integer.class, null);
            tasksTable.addContainerProperty("Member Type", String.class, null);
            tasksTable.addContainerProperty("Estimate Time", String.class, null);
            tasksTable.addContainerProperty("Assigned To", String.class, null);
            tasksTable.addContainerProperty("Complete Time", String.class, null);
            tasksTable.addContainerProperty("Edit Task", Button.class, null);
            tasksTable.addContainerProperty("View Task", Button.class, null);
            tasksTable.setSizeFull();

            int index=0;
            for(Task task:userStory.getUserStoryTasks())
            {
                index++;

                Button editTaskButton=new Button("Edit Task");
                Button viewTaskButton=new Button("View Task");
                viewTaskButton.setData(userStory.getProject().getName()+"/"+userStory.getName()+"/"+task.getTaskId());

                tasksTable.addItem(new Object[] {index,task.getName(),task.getPriority(),task.getSeverity(),task.getMemberType(),task.getEstimateTime(),task.getAssignedTo(),task.getCompleteTime(),editTaskButton,viewTaskButton},index);



                viewTaskButton.addClickListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {


                        DashboardUI.getCurrent().getNavigator().navigateTo("Schedule_Task/"+(String)event.getButton().getData());


                    }
                });


            }




    /*        viewUserStoryLayout.addComponent(tasksTable);

            Button newTaskButton= new Button("New Task");
            newTaskButton.addClickListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    TaskWindow.open(userStory);



                }
            });

            viewUserStoryLayout.addComponent(newTaskButton);*/
            viewUserStoryLayout.addComponent(tasksTable);
            viewUserStoryLayout.setExpandRatio(tasksTable,1);


        }



        //return  viewUserStoryLayout;

    }








    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label title = new Label("Task List");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        create = buildCreateReport();
        HorizontalLayout tools = new HorizontalLayout(buildFilter(),
                create);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Button buildCreateReport() {
        final Button createTask = new Button("Create New Task");
        createTask.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                TaskWindow.open(userStory);



            }
        });


        return createTask;
    }

    private Component buildFilter() {
        final TextField filter = new TextField();
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                Container.Filterable data = (Container.Filterable) tasksTable.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Container.Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId,
                                                final Item item) {

                        if (event.getText() == null
                                || event.getText().equals("")) {
                            return true;
                        }

                        return filterByProperty("Index", item,
                                event.getText())
                                || filterByProperty("Name", item,
                                event.getText());

                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        if (propertyId.equals("Index")
                                || propertyId.equals("Name")) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        filter.setInputPrompt("Filter");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.addShortcutListener(new ShortcutListener("Clear",
                ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                filter.setValue("");
                ((com.vaadin.data.Container.Filterable) tasksTable.getContainerDataSource())
                        .removeAllContainerFilters();
            }
        });
        return filter;
    }

    private boolean filterByProperty(final String prop, final Item item,
                                     final String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim()
                .toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }
}