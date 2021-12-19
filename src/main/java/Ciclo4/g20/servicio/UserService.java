/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ciclo4.g20.servicio;

import Ciclo4.g20.modelo.User;
import Ciclo4.g20.repositorio.UserRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * clase Userservice qu permite mediante metodos aaceder y validar datos 
 * del usuario y sus parametros para autenticarlos. actualizarlos y eliminarlos
 * 
 * @author Victor Vasquez
 */
@Service
public class UserService {
    //inyecta dependencias unas con otras dentro de Spring
    @Autowired
    private UserRepositorio userRepositorio;
    //lista de todos los usuarios 
    public List<User> getAll() {
        return userRepositorio.getAll();
    }
    //obtener el usuario por id
    public Optional<User> getUser(int id) {
        return userRepositorio.getUser(id);
    }
    //permita guradar o actualizar el usuario validando algunos requerimientos po id
    public User save(User user) {
        //Se obtiene el maximo id existente en la coleccion
        Optional<User> userIdMaximo = userRepositorio.lastUserId();
        //si el id del usuario que se recibe como parametro es nulo, entonces valida el maximo id
        if (user.getId() == null) {
            //valida el maximo id generado, si no hay ninguno aun el primer id sera 1
            if (userIdMaximo.isEmpty()) {
                user.setId(1);
            } //si retorna informacion suma 1 al maximo id existente y lo asigna como codigo id
            else {
                user.setId(userIdMaximo.get().getId() + 1);
            }
        }
        //vañlidando campo usuario y email si exite o no en la db user 
        Optional<User> dbUser = userRepositorio.getUser(user.getId());
        if (dbUser.isEmpty()) {
            if (emailExists(user.getEmail()) == false) {
                return userRepositorio.save(user);
            } else {
                return user;
            }
        } else {
            return user;
        }
    }
    //actualizar usuario por id validando el campo si eta vacio o no en db user
    public User update(User user) {
        if (user.getId() != null) {
            Optional<User> dbUser = userRepositorio.getUser(user.getId());
            //validando si usuario esta vacio en la db user
            if (!dbUser.isEmpty()) {
                //validando campo identificacion en db user
                if (user.getIdentification() != null) {
                    dbUser.get().setIdentification(user.getIdentification());
                }
                //validando nombre en db user
                if (user.getName() != null) {
                    dbUser.get().setName(user.getName());
                }
                //validando address en db user
                if (user.getAddress() != null) {
                    dbUser.get().setAddress(user.getAddress());
                }
                //validando cellphone en db user
                if (user.getCellPhone() != null) {
                    dbUser.get().setCellPhone(user.getCellPhone());
                }
                //validando email en db user
                if (user.getEmail() != null) {
                    dbUser.get().setEmail(user.getEmail());
                }
                //validando password en db user
                if (user.getPassword() != null) {
                    dbUser.get().setPassword(user.getPassword());
                }
                //validsndo zone en db user
                if (user.getZone() != null) {
                    dbUser.get().setZone(user.getZone());
                }
                //validando type en db user
                if (user.getType() != null) {
                    dbUser.get().setType(user.getType());
                }
                //actualizando db user 
                userRepositorio.update(dbUser.get());
                return dbUser.get();
            } else {
                return user;
            }
        }
        return user;
    }
    //metodo boleano para eliminar usuario por id 
    public boolean delete(int userId) {
        Boolean userBoolean = getUser(userId).map(user -> {
            userRepositorio.delete(user);
            return true;
        }).orElse(false);
        return userBoolean;
    }
    // metodo boleano para validar si email existe 
    public boolean emailExists(String email) {
        return userRepositorio.emailExists(email);
    }
    //validando email y password para autenticar nuevo usuario 
    public User authenticateUser(String email, String password) {
        Optional<User> user = userRepositorio.authenticateUser(email, password);
        if (user.isEmpty()) {
            return new User();
        }
        return user.get();
    }
}
