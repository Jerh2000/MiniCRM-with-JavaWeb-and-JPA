/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Producto;
import com.implDao.IProducto;
import com.utilidades.FacesUtil;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 *
 * @author Jairo
 */
public class ProductoServices extends ImplDao<Producto, Long> implements IProducto, Serializable{
    
    public Producto consultaProducto(long id) {
        EntityManager em = ImplDao.getEntityManagger();
        Producto pro = new Producto();
        em.getTransaction().begin();
        
        try {
            System.out.println(id);
            String q = "select p from Producto p where p.id = ?1";//Se cosulta a usuario en base a su login y password
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, id);
            pro = (Producto) qu.getSingleResult();//Guarda un objeto de tipo usuario que capto de la consulta
            
            System.out.println(pro.getNombre());
        } catch (Exception ex) {
            FacesUtil.addErrorMessage("Error de consulta de producto", ex.getMessage());
        } finally {
            em.close();
        }

        return pro;//Retorna el usuario consultado
    }
    public Producto agregarAStock(String ref, int stock) {
        EntityManager em = ImplDao.getEntityManagger();
        Producto pro = new Producto();
        em.getTransaction().begin();
        
        try {
            
            String q = "update Producto set stockm = ?1 where referencia = ?2";//Se cosulta a usuario en base a su login y password
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, stock)
                    .setParameter(2, ref);
            pro = (Producto) qu.getSingleResult();//Guarda un objeto de tipo usuario que capto de la consulta
            
            System.out.println(pro.getNombre());
        } catch (Exception ex) {
            FacesUtil.addErrorMessage("Error de actualizar de producto", ex.getMessage());
        } finally {
            em.close();
        }

        return pro;//Retorna el usuario consultado
    }
}
