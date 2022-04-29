package com.teste.crud.Repository;

import com.teste.crud.Model.CarroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarroRepository extends JpaRepository<CarroModel,Integer> {
}
