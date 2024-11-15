<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css'/>
        <title>Crear Árbitro</title>
    </head>
    <body>
        <jsp:include page="../includes/navbar.jsp" />
        <div class='container'>
            <h1 class='mb-3'>Crear Árbitro</h1>
            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger">
                <%= request.getAttribute("error") %>
            </div>
            <% } %>
            <form method="POST" action="<%= request.getContextPath() %>/ArbitroServlet?action=guardar">
                <div class="form-group">
                    <label>Nombre</label>
                    <input type="text" class="form-control" name="nombre" required>
                </div>
                <div class="form-group">
                    <label>País</label>
                    <input type="text" class="form-control" name="pais" required>
                </div>
                <button type="submit" class="btn btn-primary">Guardar</button>
                <a href="<%= request.getContextPath() %>/ArbitroServlet" class="btn btn-danger">Cancelar</a>
            </form>
        </div>
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    </body>
</html>
