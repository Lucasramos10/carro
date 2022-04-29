package com.teste.crud.Controller;

import com.teste.crud.Model.CarroModel;
import com.teste.crud.Repository.CarroRepository;
import com.teste.crud.dto.RequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@Tag(name = "Carros")
public class CarroController {
    CarroRepository repository;

    @Autowired
    public CarroController(CarroRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/carros")
    @Operation(description = "Listar carros")
    public ResponseEntity<List<CarroModel>> getCarros() {
        List<CarroModel> carro = repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        if (carro.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            for (CarroModel carroModel : carro) {
                Integer id = carroModel.getId();
                carroModel.add(linkTo(methodOn(CarroController.class).getCarrosById(id)).withSelfRel());
            }
            return new ResponseEntity<>(carro, HttpStatus.OK);
        }
    }

    @GetMapping("/carros/{id}")
    public ResponseEntity<CarroModel> getCarrosById(@PathVariable(value = "id") Integer id) {
        Optional<CarroModel> carro = repository.findById(id);
        if (carro.isPresent()) {
            carro.get().add(linkTo(methodOn(CarroController.class).getCarros()).withRel("Lista de Carros"));
            return new ResponseEntity<>(carro.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/carros")
    @Operation(description = "Inserir novo carro")
    public ResponseEntity<?> createCarro(@RequestBody @Valid RequestDto request) {
        CarroModel carro = new CarroModel();
        CarroModel response = repository.save(carro);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/carros/{id}")
    public ResponseEntity<CarroModel> updateCarro(@PathVariable("id") Integer id, @RequestBody  @Valid RequestDto request) {
        CarroModel carro = new CarroModel();
        Optional<CarroModel> carroModel = repository.findById(id);

        if (carroModel.isPresent()) {
            CarroModel carroPut = carroModel.get();
            carroPut.setMarca(carro.getMarca());
            carroPut.setModelo(carro.getModelo());
            carroPut.setAno(carro.getAno());
            carroPut.setCor(carro.getCor());
            return new ResponseEntity<>(repository.save(carroPut), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/carros/{id}")
    public ResponseEntity<?> deleteCarrosById(@PathVariable("id") Integer id) {
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/carros")
    public ResponseEntity<HttpStatus> deleteCarros() {
        repository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}