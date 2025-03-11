package com.file.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.file.entity.Roles;

public interface RolesRepo extends JpaRepository<Roles, Long>  
{

}
