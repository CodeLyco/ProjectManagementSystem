package com.pms.dao;

import com.pms.domain.Project;
import com.pms.domain.Task;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Damitha on 6/2/2015.
 */
public class TaskDAO {

    private SessionFactory sessionFactory;



    public Task getTaskByTaskId(int id)
    {
        Session session = getSessionFactory().openSession();
        String SQL_QUERY = "from Task as task  where task.id=id ";
        Query query = session.createQuery(SQL_QUERY);
        List<Task> list = ((org.hibernate.Query) query).list();

        if(list.size()>0)
        {
            return list.get(0);
        }
        return null;

    }

    public void removeTask(Task task)
    {

    }

















    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


}
