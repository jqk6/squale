<%@ page import="org.squale.squaleweb.resources.WebMessages" %>
<%-- Le marquage XITI de la page (� placer dans le body de la page) --%>
<%
	// Cette page sera vide pour le dev et la recette afin de ne pas avoir de marqueur dans ses environnements
	String useXITI = WebMessages.getString(request, "xiti.use");
	if(useXITI.equals("true")) {
%>
<%
	/* On r�cup�re les param�tres n�cessaire aux marquages */
	// Le param�tre sp�cifique � la page pass� en param�tre dans le jsp:include
	String pageName = (String) request.getParameter("page");
	// L'id de niveau 1
	String firstId = WebMessages.getString(request, "xiti.configuration.id.first_level");
	// L'id de niveau 2
	String secondId = WebMessages.getString(request, "xiti.configuration.id.second_level");
%>
<script type="text/javascript">xt_page_AF_v2(<%=firstId%>, <%=secondId%>, "<%=pageName%>");</script>
<%
	}
%>