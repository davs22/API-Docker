package com.example.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.model.Administrador;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/Administrador")
public class AdministradorController {
    private List<Administrador> adms = new ArrayList<>();

    public AdministradorController() {
        adms.add(new Administrador(1L, "Matheus", "matheus@gmail.com", "Masculino"));
        adms.add(new Administrador(2L, "Marcus", "marcus@gmail.com", "Masculino"));
    }

    @GetMapping
    public List<Administrador> listar() {
        return adms;
    }

    @GetMapping("/{id}")
    public Administrador buscarPorId(@PathVariable Long id) {
        return adms.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public Administrador criar (@RequestBody Administrador adm) {
        Long novoId = adms.stream()
                .mapToLong(Administrador::getId)
                .max()
                .orElse(0L) + 1;
        adm.setId(novoId);
        adms.add(adm);
        return adm;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Administrador> atualizar(@PathVariable Long id, @RequestBody Administrador admAtualizado) {
        for (Administrador adm : adms) {
            if (adm.getId().equals(id)) {
                adm.setNome(admAtualizado.getNome());
                adm.setEmail(admAtualizado.getEmail());
                adm.setSexo(admAtualizado.getSexo());
                return ResponseEntity.ok(adm);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Administrador> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        
        Optional<Administrador> optionalAdm = adms.stream()
                .filter(adm -> adm.getId().equals(id))
                .findFirst();
        if (optionalAdm.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Administrador adm = optionalAdm.get();

        updates.forEach((chave, valor) -> {
            switch (chave) {
                case "nome":
                    adm.setNome((String) valor);
                    break;
                case "email":
                    adm.setEmail((String) valor);
                    break;
            }
        });

        return ResponseEntity.ok(adm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removido = adms.removeIf(c -> c.getId().equals(id));

        if (removido) {
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

}
