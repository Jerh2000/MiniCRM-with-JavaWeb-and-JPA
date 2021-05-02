/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.DetalleVenta;
import com.entity.Venta;
import com.implDao.IDetalleVenta;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 *
 * @author Jairo
 */
public class DetalleVentaServices extends ImplDao<DetalleVenta, Long> implements IDetalleVenta, Serializable{
     
    public List<DetalleVenta> obtenerDetallesCompra(Venta v) {
        EntityManager em = ImplDao.getEntityManagger();
        List<DetalleVenta> detVenta = new LinkedList<>();
        em.getTransaction().begin();

        String q = "select d from DetalleVenta d where d.venta.id = ?1";//Se cosulta a los DETcotizaciones
        System.out.println(" Consulta: " + q);
        Query qu = em.createQuery(q)
                .setParameter(1, v.getId());
        detVenta = qu.getResultList();

        return detVenta;//Retorna las DETcompra solicitadas en lista
    }
}
