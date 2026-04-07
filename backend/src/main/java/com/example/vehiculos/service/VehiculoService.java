package com.example.vehiculos.service;

import com.example.vehiculos.model.Alerta;
import com.example.vehiculos.model.Vehiculo;
import com.example.vehiculos.model.VehiculoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class VehiculoService {

    private final List<Vehiculo> vehiculos = new ArrayList<>(List.of(
            new Vehiculo("AAA111", 40),
            new Vehiculo("BBB222", 90),
            new Vehiculo("CCC333!", 120)
    ));

    public List<VehiculoResponse> obtenerVehiculos(Optional<Alerta> alerta) {
        return vehiculos.stream()
                .map(this::toResponse)
                .filter(vehiculo -> alerta.map(value -> value == vehiculo.alerta()).orElse(true))
                .toList();
    }

    public VehiculoResponse crearVehiculo(String placa) {
        String placaNormalizada = normalizarPlaca(placa);

        boolean existe = vehiculos.stream()
                .anyMatch(vehiculo -> vehiculo.placa().equalsIgnoreCase(placaNormalizada));

        if (existe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La placa ya existe");
        }

        Vehiculo nuevoVehiculo = new Vehiculo(placaNormalizada, 0);
        vehiculos.add(nuevoVehiculo);
        return toResponse(nuevoVehiculo);
    }

    public VehiculoResponse actualizarVelocidad(String placa, int velocidad) {
        for (int index = 0; index < vehiculos.size(); index++) {
            Vehiculo actual = vehiculos.get(index);
            if (actual.placa().equalsIgnoreCase(placa)) {
                Vehiculo actualizado = new Vehiculo(actual.placa(), velocidad);
                vehiculos.set(index, actualizado);
                return toResponse(actualizado);
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado");
    }

    private VehiculoResponse toResponse(Vehiculo vehiculo) {
        return new VehiculoResponse(vehiculo.placa(), vehiculo.velocidad(), calcularAlerta(vehiculo.velocidad()));
    }

    private Alerta calcularAlerta(int velocidad) {
        if (velocidad < 0) {
            return Alerta.RETRO;
        }
        if (velocidad == 0) {
            return Alerta.PARADO;
        }
        if (velocidad > 100) {
            return Alerta.CRITICO;
        }
        if (velocidad >= 80) {
            return Alerta.RIESGO;
        }
        return Alerta.OK;
    }

    private String normalizarPlaca(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La placa es obligatoria");
        }

        return placa.trim().toUpperCase(Locale.ROOT);
    }
}
