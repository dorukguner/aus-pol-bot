package com.dguner.auspolbot.db.repositories;

import com.dguner.auspolbot.db.entities.Bill;
import org.springframework.data.repository.CrudRepository;

public interface BillRepository extends CrudRepository<Bill, String> {

}
