package com.example.lab9_base.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import com.example.lab9_base.Dao.DaoArbitros;
import com.example.lab9_base.Bean.Arbitro;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ArbitroServlet", urlPatterns = {"/ArbitroServlet"})
public class ArbitroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;

        DaoArbitros daoArbitros = new DaoArbitros();

        switch (action) {
            case "guardar":
                // Obtener los parámetros del formulario
                String nombre = request.getParameter("nombre");
                String pais = request.getParameter("pais");

                // Validar que los campos no estén vacíos
                if (nombre == null || nombre.trim().isEmpty() || pais == null || pais.trim().isEmpty()) {
                    // Si algún campo está vacío, mostrar un mensaje de error y regresar al formulario
                    request.setAttribute("error", "Todos los campos son obligatorios.");
                    view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                    return;
                }

                // Verificar si el nombre del árbitro ya existe en la base de datos
                DaoArbitros arbitroDao = new DaoArbitros();
                if (arbitroDao.existeNombreArbitro(nombre)) {
                    // Si el nombre ya existe, mostrar un mensaje de error
                    request.setAttribute("error", "El nombre del árbitro ya existe. Por favor, elige otro nombre.");
                    view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                    return;
                }

                // Crear un nuevo objeto Arbitro con los datos del formulario
                Arbitro nuevoArbitro = new Arbitro();
                nuevoArbitro.setNombre(nombre);
                nuevoArbitro.setPais(pais);

                // Guardar el árbitro en la base de datos usando el DAO
                boolean success = daoArbitros.crearArbitro(nuevoArbitro);


                if (success) {
                    // Redirigir a la lista de árbitros si el registro es exitoso
                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet");
                } else {
                    // Mostrar un mensaje de error si el registro falla
                    request.setAttribute("error", "Hubo un problema al guardar el árbitro. Inténtalo de nuevo.");
                    view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                }
                break;

            case "buscar":
                // Mover la lógica de búsqueda aquí si se usa el método POST
                String tipo = request.getParameter("tipo");
                String buscar = request.getParameter("buscar");

                ArrayList<Arbitro> arbitros = new ArrayList<>();
                if ("nombre".equalsIgnoreCase(tipo)) {
                    arbitros = daoArbitros.busquedaNombre(buscar);
                } else if ("pais".equalsIgnoreCase(tipo)) {
                    arbitros = daoArbitros.busquedaPais(buscar);
                }

                request.setAttribute("arbitros", arbitros);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;

        DaoArbitros daoArbitros = new DaoArbitros();

        switch (action) {

            case "buscar":
                // Obtén los parámetros de búsqueda del request
                String tipo = request.getParameter("tipo"); // Puede ser "nombre" o "pais"
                String buscar = request.getParameter("buscar"); // Texto ingresado por el usuario

                ArrayList<Arbitro> arbitros = new ArrayList<>();
                if ("nombre".equalsIgnoreCase(tipo)) {
                    arbitros = daoArbitros.busquedaNombre(buscar);
                } else if ("pais".equalsIgnoreCase(tipo)) {
                    arbitros = daoArbitros.busquedaPais(buscar);
                }

                // Pasa la lista de árbitros filtrados al JSP
                request.setAttribute("arbitros", arbitros);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;

            case "lista":
                // Mostrar la lista de árbitros
                ArrayList<Arbitro> listaArbitros = daoArbitros.listaArbitros();
                request.setAttribute("arbitros", listaArbitros);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;

            case "crear":
                // Preparar la vista de creación con la lista de países
                ArrayList<String> paises = new ArrayList<>();
                paises.add("Peru");
                paises.add("Chile");
                paises.add("Argentina");
                paises.add("Paraguay");
                paises.add("Uruguay");
                paises.add("Colombia");

                request.setAttribute("paises", paises);
                view = request.getRequestDispatcher("/arbitros/form.jsp");
                view.forward(request, response);
                break;

            case "borrar":
                // Obtener el ID del árbitro a eliminar desde los parámetros de la solicitud
                int idArbitro;
                try {
                    idArbitro = Integer.parseInt(request.getParameter("id"));
                    DaoArbitros dao = new DaoArbitros();
                    boolean success = dao.eliminarArbitro(idArbitro);

                    if (success) {
                        // Redirigir a la lista de árbitros después de la eliminación
                        response.sendRedirect(request.getContextPath() + "/ArbitroServlet");
                    } else {
                        // Manejar el error si no se pudo eliminar
                        request.setAttribute("error", "Hubo un problema al eliminar el árbitro.");
                        view = request.getRequestDispatcher("/arbitros/list.jsp");
                        view.forward(request, response);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    request.setAttribute("error", "ID de árbitro inválido.");
                    view = request.getRequestDispatcher("/arbitros/list.jsp");
                    view.forward(request, response);
                }
                break;
        }
    }

}

