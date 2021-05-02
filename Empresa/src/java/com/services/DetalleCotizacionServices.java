/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Cotizacion;
import com.entity.DetalleCotizacion;
import com.implDao.IDetalleCotizacion;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jairo
 */
public class DetalleCotizacionServices extends ImplDao<DetalleCotizacion, Long> implements IDetalleCotizacion, Serializable{
    
    public List<DetalleCotizacion> obtenerDetallesCotizacion(Cotizacion c) {
        EntityManager em = ImplDao.getEntityManagger();
        List<DetalleCotizacion> detCotizacion = new LinkedList<>();
        em.getTransaction().begin();

        String q = "select d from DetalleCotizacion d where d.cotizacion.id = ?1";//Se cosulta a los DETcotizaciones
        System.out.println(" Consulta: " + q);
        Query qu = em.createQuery(q)
                .setParameter(1, c.getId());
        detCotizacion = qu.getResultList();

        return detCotizacion;//Retorna las DETcotizaciones solicitadas en lista
    }
}
