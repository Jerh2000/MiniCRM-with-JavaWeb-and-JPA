/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Cliente;
import com.entity.Empleado;
import com.entity.Venta;
import com.implDao.IVenta;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 *
 * @author Jairo
 */
public class VentaServices extends ImplDao<Venta, Long> implements IVenta, Serializable{
     
    public List<Venta> obtenerComprasCliente(Cliente c) {
        EntityManager em = ImplDao.getEntityManagger();
        List<Venta> comprasCliente = new LinkedList<>();
        em.getTransaction().begin();
        String q = "select v from Venta v where v.cliente.id = ?1";
        System.out.println(" Consulta: " + q);
        Query qu = em.createQuery(q)
                .setParameter(1, c.getId());
        comprasCliente = qu.getResultList();

        return comprasCliente;
    }
    public List<Venta> obtenerComprasEmpleado(Empleado e) {
        EntityManager em = ImplDao.getEntityManagger();
        List<Venta> ventasE = new LinkedList<>();
        em.getTransaction().begin();
        String q = "select v from Venta v where v.empleado.id = ?1";
        System.out.println(" Consulta: " + q);
        Query qu = em.createQuery(q)
                .setParameter(1, e.getId());
        ventasE = qu.getResultList();

        return ventasE;
    }
}
