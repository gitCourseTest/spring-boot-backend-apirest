package com.bolsadeideas.springbootbackendapirest.models.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springbootbackendapirest.models.dao.IClienteDao;
import com.bolsadeideas.springbootbackendapirest.models.entity.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService{
	
	@Autowired
	private IClienteDao clienteDao;

	@Override
	@Transactional(readOnly=true)//de solo lecutura porque es un select
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	@Transactional(readOnly=true)//de solo lecutura porque es un select
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);//Si lo encuentra devuelve el objeto, sino devuelve null.
	}

	@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		return clienteDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);		
	}

	@Override
	@Transactional
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

}
