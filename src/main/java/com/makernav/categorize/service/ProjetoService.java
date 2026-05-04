package com.makernav.categorize.service;

import com.makernav.categorize.infra.repository.ProjetoRepository;
import com.makernav.categorize.model.Projeto;
import com.makernav.categorize.infra.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    public List<Projeto> listarTodos() {
        return projetoRepository.findAll();
    }

    public Optional<Projeto> buscarPorId(int id) {
        return projetoRepository.findById(id);
    }

    public Projeto salvar(Projeto projeto) {
        return projetoRepository.save(projeto);
    }

    public void deletar(int id) {
        projetoRepository.deleteById(id);
    }
}
