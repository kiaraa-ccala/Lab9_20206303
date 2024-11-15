package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Dao.DaoArbitros;
import com.example.lab9_base.Dao.DaoPartidos;
import com.example.lab9_base.Dao.DaoSelecciones;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "PartidoServlet", urlPatterns = {"/PartidoServlet"})
public class PartidoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");
        RequestDispatcher view;

        switch (action) {
            case "guardar":
                // Obtener y validar los parámetros del formulario
                String jornadaStr = request.getParameter("jornada");
                String fecha = request.getParameter("fecha");
                String seleccionLocal = request.getParameter("local");
                String seleccionVisitante = request.getParameter("visitante");
                String arbitro = request.getParameter("arbitro");

                // Validaciones básicas
                if (jornadaStr == null || fecha == null || seleccionLocal == null || seleccionVisitante == null || arbitro == null ||
                        jornadaStr.trim().isEmpty() || fecha.trim().isEmpty() || seleccionLocal.trim().isEmpty() || seleccionVisitante.trim().isEmpty() || arbitro.trim().isEmpty() ||
                        seleccionLocal.equals(seleccionVisitante)) {
                    // Si algún campo está vacío o las selecciones son iguales, mostrar error
                    request.setAttribute("error", "Todos los campos son obligatorios y las selecciones deben ser diferentes.");
                    view = request.getRequestDispatcher("/partidos/form.jsp");
                    view.forward(request, response);
                    return;
                }

                // Convertir y configurar los datos
                int jornada = Integer.parseInt(jornadaStr);

                Partido nuevoPartido = new Partido();
                nuevoPartido.setNumeroJornada(jornada);
                nuevoPartido.setFecha(java.sql.Date.valueOf(fecha));

                // Configurar selección local
                Seleccion localSeleccion = new Seleccion();
                localSeleccion.setIdSeleccion(Integer.parseInt(seleccionLocal));
                nuevoPartido.setSeleccionLocal(localSeleccion);

                // Configurar selección visitante
                Seleccion visitanteSeleccion = new Seleccion();
                visitanteSeleccion.setIdSeleccion(Integer.parseInt(seleccionVisitante));
                nuevoPartido.setSeleccionVisitante(visitanteSeleccion);

                // Configurar el árbitro
                Arbitro arbitroObj = new Arbitro();
                arbitroObj.setIdArbitro(Integer.parseInt(arbitro));
                nuevoPartido.setArbitro(arbitroObj);

                // Crear una instancia del DAO y verificar si el partido ya existe
                DaoPartidos daoPartidos = new DaoPartidos();
                if (daoPartidos.existePartido(nuevoPartido)) {
                    // Si el partido ya existe, mostrar un mensaje de error
                    request.setAttribute("error", "El partido ya existe en la base de datos.");
                    view = request.getRequestDispatcher("/partidos/form.jsp");
                    view.forward(request, response);
                    return;
                }

                // Guardar el partido usando el DAO
                boolean success = daoPartidos.crearPartido(nuevoPartido);

                if (success) {
                    // Redirigir a la lista de partidos si se guarda correctamente
                    response.sendRedirect(request.getContextPath() + "/PartidoServlet");
                } else {
                    // Mostrar un mensaje de error si no se guarda
                    request.setAttribute("error", "Hubo un problema al guardar el partido. Inténtalo de nuevo.");
                    view = request.getRequestDispatcher("/partidos/form.jsp");
                    view.forward(request, response);
                }
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;

        switch (action) {
            case "crear":
                // Crear una lista de selecciones y pasarla al JSP
                DaoSelecciones daoSelecciones = new DaoSelecciones();
                ArrayList<Seleccion> selecciones = daoSelecciones.listarSelecciones();
                request.setAttribute("selecciones", selecciones);

                // Crear una lista de árbitros y pasarla al JSP
                ArrayList<Arbitro> arbitros = new ArrayList<>();
                // Rellena la lista de árbitros con datos de la base de datos o de un método DAO
                DaoArbitros daoArbitros = new DaoArbitros(); // Asegúrate de tener un DaoArbitros para manejar esto
                arbitros = daoArbitros.listaArbitros();
                // Llama a un método que devuelva la lista de árbitros
                request.setAttribute("arbitros", arbitros);

                view = request.getRequestDispatcher("/partidos/form.jsp");
                view.forward(request, response);
                break;

            case "lista":
                // Código para listar partidos
                DaoPartidos daoPartidos = new DaoPartidos();
                ArrayList<Partido> partidos = daoPartidos.listaDePartidos();
                request.setAttribute("partidos", partidos);
                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
                break;
        }
    }


}
