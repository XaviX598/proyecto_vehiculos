package com.example.vehiculos.controller;

import com.example.vehiculos.model.ActualizarVelocidadRequest;
import com.example.vehiculos.model.Alerta;
import com.example.vehiculos.model.CrearVehiculoRequest;
import com.example.vehiculos.model.VehiculoResponse;
import com.example.vehiculos.service.VehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/vehiculos")
@CrossOrigin(origins = "http://localhost:5173")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<VehiculoResponse> listarVehiculos(@RequestParam Optional<String> alerta) {
        Optional<Alerta> alertaFiltro = alerta
                .filter(value -> !value.isBlank())
                .map(this::parsearAlerta);

        return vehiculoService.obtenerVehiculos(alertaFiltro);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculoResponse crearVehiculo(@RequestBody CrearVehiculoRequest request) {
        return vehiculoService.crearVehiculo(request.placa());
    }

    @PutMapping("/{placa}/velocidad")
    public VehiculoResponse actualizarVelocidad(
            @PathVariable String placa,
            @RequestBody ActualizarVelocidadRequest request
    ) {
        return vehiculoService.actualizarVelocidad(placa, request.velocidad());
    }

    private Alerta parsearAlerta(String alerta) {
        try {
            return Alerta.valueOf(alerta.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El filtro alerta debe ser RETRO, PARADO, OK, RIESGO o CRITICO"
            );
        }
    }
}
