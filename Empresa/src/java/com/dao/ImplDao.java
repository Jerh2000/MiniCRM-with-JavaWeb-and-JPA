/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.utilidades.FacesUtil;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;



/**
 *
 * @author Jairo
 */

public class ImplDao<T, ID> implements IDao<T, ID> {

    private static EntityManagerFactory emf;

    static {
        setEmf(Persistence.createEntityManagerFactory("EmpresaPU"));
    }

    /**
     * @return the emf
     */
    public static EntityManagerFactory getEmf() {
        return emf;
    }

    /**
     * @param aEmf the emf to set
     */
    public static void setEmf(EntityManagerFactory aEmf) {
        emf = aEmf;
    }


    public static EntityManager getEntityManagger() {
        return getEmf().createEntityManager();
    }

    @Override
    public void crear(T entity) {
        //System.out.println(""+entity);
        EntityManager em = getEntityManagger();
        EntityTransaction et=em.getTransaction();
        try{
        et.begin();
        em.merge(entity);
        et.commit();
        //FacesUtil.addInfoMessage("Se almacenó en el sistema el siguiente Elemento: "+entity.getClass().getSimpleName());
        }
        catch(PersistenceException pe){
            pe.printStackTrace();
            //et.rollback();
            FacesUtil.addErrorMessage("Error de Persistencia: "+pe.getMessage());
        }
        finally{
          if (em != null && em.isOpen()) {
            if (em.getTransaction() != null && 
                em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
            em = null;
            }
        }
    }

    public void cerrarS(){
      
    }
        
    @Override
    public <T> T consultar(Class<T> entityClass, Object primaryKey) {
        EntityManager em = getEntityManagger();
        EntityTransaction et=em.getTransaction();
        et.begin();
        T ent=em.find(entityClass, primaryKey);
        et.commit();
        em.close();
        return ent;
    }

     public <T> T consultarC(Class<T> entityClass, Object primaryKey) {
        EntityManager em = getEntityManagger();
        em.getTransaction().begin();
        T ent=em.find(entityClass, primaryKey);
        em.close();
        return ent;
    }

    
    @Override
    public <T> T modificar(T entity) {
        T ent = null;        
        EntityManager em = getEntityManagger();
        EntityTransaction et=em.getTransaction();
        try{
        et.begin();       
        ent=em.merge(entity);
        et.commit();
//        FacesUtil.addInfoMessage("Se Actualizó en el sistema el siguiente Elemento: "+entity.getClass().getSimpleName());
        }catch(PersistenceException pe){   
//            FacesUtil.addErrorMessage("Error de Persistencia: "+pe.getMessage());
//            if(et.isActive()){
//                et.rollback();            
//            }
            pe.printStackTrace();
        }
        catch(Exception e){
            if(e instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException){
                FacesUtil.addErrorMessage("El codigo del producto es obligatorio y debe ser unico: "+e.getMessage());
            }
            FacesUtil.addErrorMessage("Error de Persistencia: "+e.getMessage());
        }
        finally{
            em.close();
        }
        return ent;
    }

    @Override
    public void eliminar(Object entity) {
        EntityManager em = getEntityManagger();
        EntityTransaction et=em.getTransaction();
        try{
        em.getTransaction().begin();
//        et.begin();
        entity=em.merge(entity);
        em.remove(entity);
        em.getTransaction().commit();
//        et.commit();
        }catch(PersistenceException pe){
           em.getTransaction().rollback();
            FacesUtil.addErrorMessage("Error de Persistencia: "+pe.getMessage());
        }finally{
        em.close();
        }
    }

    @Override
    public List<T> consultarTodo(Class<T> entityClass) {
        List<T> entidades;
        EntityManager em = getEntityManagger();
        em.getTransaction().begin();
        String clase=entityClass.getSimpleName();
        //System.out.println(clase);
        entidades=em.createQuery("select t from "+clase+" t").getResultList();
        em.close();
        return entidades;
    }

 
}
