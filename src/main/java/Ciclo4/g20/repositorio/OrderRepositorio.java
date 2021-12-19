/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ciclo4.g20.repositorio;

import Ciclo4.g20.modelo.Order;
import Ciclo4.g20.repositorio.crud.OrderCrudRepositorio;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author victor vasquez
 */
@Repository
public class OrderRepositorio {
    @Autowired
    private OrderCrudRepositorio orderCrudRepositorio;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Order> getAll() {
        return orderCrudRepositorio.findAll();
    }

    public Optional<Order> getOrder(int id) {
        return orderCrudRepositorio.findById(id);
    }

    public Order save(Order order){
        return orderCrudRepositorio.save(order);
    }
    
    public void update(Order order){
        orderCrudRepositorio.save(order);
    }
    
    public void delete(Order order){
        orderCrudRepositorio.delete(order);
    } 
    
    public Optional<Order> LastOrderId(){
        return orderCrudRepositorio.findTopByOrderByIdDesc();
    }
    
    public List<Order> findByZone(String zona){
        return orderCrudRepositorio.findByZone(zona);
    }
    
    //Reto 4: Ordenes de un asesor
    public List<Order> ordersSalesManByID(Integer id) {

        Query query = new Query();
        Criteria dateCriteria = Criteria.where("salesMan.id").is(id);

        query.addCriteria(dateCriteria);
        List<Order> orders = mongoTemplate.find(query, Order.class);

        return orders;
    }
    
    //Reto 4: Ordenes de un asesor x Fecha
    public List<Order> ordersSalesManByDate(String dateStr, Integer id) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Query query = new Query();
        Criteria dateCriteria = Criteria.where("registerDay")
                .gte(LocalDate.parse(dateStr, dtf).minusDays(1).atStartOfDay())
                .lt(LocalDate.parse(dateStr, dtf).plusDays(2).atStartOfDay())
                .and("salesMan.id").is(id);

        query.addCriteria(dateCriteria);
        List<Order> orders = mongoTemplate.find(query, Order.class);

        return orders;
    }   
    
    //Reto 4: Ordenes de un asesor x Estado
    public List<Order> ordersSalesManByState(String state, Integer id) {

        Query query = new Query();
        Criteria dateCriteria = Criteria.where("salesMan.id").is(id)
                .and("status").is(state);

        query.addCriteria(dateCriteria);
        List<Order> orders = mongoTemplate.find(query, Order.class);

        return orders;
    }    
}
