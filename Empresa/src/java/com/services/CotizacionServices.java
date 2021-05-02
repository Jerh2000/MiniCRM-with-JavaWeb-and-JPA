/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Cliente;
import com.entity.Cotizacion;
import com.entity.Empleado;
import com.implDao.ICotizacion;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jairo
 */
public class CotizacionServices extends ImplDao<Cotizacion, Long> implements ICotizacion, Serializable {

    public List<Cotizacion> obtenerCotizacionesCliente(Cliente c) {
        EntityManager em = ImplDao.getEntityManagger();
        List<Cotizacion> cotizacionesCliente = new LinkedList<>();
        em.getTransaction().begin();

        String q = "select c from Cotizacion c where c.cliente.id = ?1";//Se cosulta a las cotizaciones en base a su estado
        System.out.println(" Consulta: " + q);
        Query qu = em.createQuery(q)
                .setParameter(1, c.getId());
        cotizacionesCliente = qu.getResultList();

        return cotizacionesCliente;//Retorna las cotizaciones solicitadas en lista
    }
    
    public List<Cotizacion> obtenerCotizacionesEmpleado(Empleado e) {
        EntityManager em = ImplDao.getEntityManagger();
        List<Cotizacion> cotizacionesEmpleado = new LinkedList<>();
        em.getTransaction().begin();

        String q = "select c from Cotizacion c where c.empleado.id = ?1";//Se cosulta a las cotizaciones en base a su estado
        System.out.println(" Consulta: " + q);
        Query qu = em.createQuery(q)
                .setParameter(1, e.getId());
        cotizacionesEmpleado = qu.getResultList();

        return cotizacionesEmpleado;//Retorna las cotizaciones solicitadas en lista
    }

}
