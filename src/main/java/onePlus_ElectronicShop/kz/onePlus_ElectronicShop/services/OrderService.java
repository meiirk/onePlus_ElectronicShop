package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.services;

import lombok.RequiredArgsConstructor;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.Orders;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.repositories.OrderRepasitory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepasitory orderRepasitory;


    public List<Orders> getOrders(){
        return orderRepasitory.findAll();
    }
    public Orders addOrder(Orders order){
        return orderRepasitory.save(order);
    }


}
