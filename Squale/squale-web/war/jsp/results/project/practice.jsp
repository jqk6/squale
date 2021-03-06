<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@taglib uri="http://www.squale.org/welcom/tags-welcom" prefix="af"%>
<%@taglib uri="/squale" prefix="squale"%>

<%@ page import="org.squale.squaleweb.resources.WebMessages" %>
<%@ page import="org.squale.squaleweb.applicationlayer.formbean.results.ProjectSummaryForm" %>
<%@ page import="org.squale.squaleweb.util.graph.GraphMaker"%>
<%@ page import="org.squale.squaleweb.tagslib.HistoryTag"%>

<script type="text/javascript"
	src="theme/charte_v03_001/js/tagManagement.js"></script>
<script type="text/javascript" src="jslib/jquery.js"></script>

<bean:define id="projectId" name="projectSummaryForm" property="projectId" type="String" />
<bean:define id="currentAuditId" name="projectSummaryForm"	property="currentAuditId" type="String" />
<bean:define id="previousAuditId" name="projectSummaryForm"	property="previousAuditId" type="String" />
<bean:define id="form" name="projectSummaryForm" property="results" />
<bean:define id="practiceName" name="form" property="name" type="String" />
<bean:define id="repartition" name="form" property="intRepartition"	type="double[]" />
<bean:define id="treId" name="form" property="id" type="String" />
<bean:define id="infoForm" name="form" property="infoForm" />
<%
// On met le form dans la requ�te pour que l'inclusion
// de la page jsp puisse trouver le bean dans le scope request
request.setAttribute("practiceInformationForm", infoForm);
//Parameter indicates which tab must be selected
String selectedTab = request.getParameter(HistoryTag.SELECTED_TAB_KEY);
if(selectedTab == null) {
    // First tab by default
    selectedTab = "factors";
}
%>


<af:page titleKey="project.results.title">
	<af:head>
		<%-- inclusion pour le marquage XITI --%>
		<jsp:include page="/jsp/xiti/xiti_header_common.jsp" />
	</af:head>
	<af:body canvasLeftPageInclude="/jsp/canvas/project_menu.jsp">
		<squale:tracker directWay="false" projectId="<%=projectId%>"
			currentAuditId="<%=currentAuditId%>"
			previousAuditId="<%=previousAuditId%>" />
	
		<af:canvasCenter>
			<%-- inclusion pour le marquage XITI sp�cifique � la page--%>
			<jsp:include page="/jsp/xiti/xiti_body_common.jsp">
				<jsp:param name="page" value="Consultation::Pratique" />
			</jsp:include>

			<br />
			<squale:resultsHeader name="projectSummaryForm" displayComparable="true"/>

			<br />
			<h2><bean:message key="project.results.practice.subtitle"
				arg0="<%=WebMessages.getString(request, practiceName)%>" /></h2>
			<br />
			<table class="tblh" style="width: 20%">
				<thead>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th><bean:message key="project.result.practice.value" /></th>
						<td class="weatherInfo">
							<squale:mark name="form" mark="currentMark" /> 
							<squale:picto name="form" property="currentMark" />
							<squale:trend name="form" current="currentMark" predecessor="predecessorMark" />
						</td>
					</tr>
				</tbody>
			</table>
			<br />	
			<br />			
			
			<af:tabbedPane name="practiceDetail">
			
				<%-- Main TAB : display basic repartition --%>
				<af:tab key="project.results.practice.graphes.global" name="main"
					lazyLoading="false"
					isTabSelected='<%=""+selectedTab.equals("main")%>'>
					<div style="background-color: white; padding: 10px">
						<%-- 1- DISPLAY : help --%>
						<br />
						<af:dropDownPanel titleKey="buttonTag.menu.aide">
							<bean:message key="project.result.practice.intro" />
						</af:dropDownPanel>
						<br />
						<%-- 2- DISPLAY : weather-like repartition --%>
						<table class="tblh" style="width: 50%">
							<THEAD>
								<tr>
									<th class="sort" colspan="6">&nbsp;</th>
								</tr>
							</THEAD>
							<TBODY>
								<tr>
									<th><bean:message key="project.result.practice.value" /></th>
									<logic:iterate id="repartitionId" name="repartition"
										indexId="counter">
										<!-- c'est pour l'affichage du nombre d'�l�ments des indexes 0,1,2,3 ou 4 -->
										<logic:greaterThan name="repartitionId" value="0">
											<logic:notEmpty name="form" property="parentId">
												<bean:define id="parentId" name="form" property="parentId"
													type="String" />
												<td>
												<div align="center"><A
													HREF='<%="mark.do?action=mark&projectId=" + projectId + "&currentAuditId=" + currentAuditId + "&previousAuditId=" + previousAuditId + "&tre=" + treId + "&factorParent=" + parentId + "&currentMark=" + counter%>'
													class="nobottom"> <squale:picto name="" property=""
													mark="<%=counter.toString()%>" /> </A></div>
												</td>
											</logic:notEmpty>
											<logic:empty name="form" property="parentId">
												<td>
												<div align="center"><A
													HREF='<%="mark.do?action=mark&projectId=" + projectId + "&currentAuditId=" + currentAuditId + "&previousAuditId=" + previousAuditId + "&tre=" + treId + "&currentMark=" + counter%>'
													class="nobottom"> <squale:picto name="" property=""
													mark="<%=counter.toString()%>" /> </A></div>
												</td>
											</logic:empty>
										</logic:greaterThan>
										<logic:equal name="repartitionId" value="0">
											<td>
											<div align="center"><squale:picto name="" property=""
												mark="<%=counter.toString()%>" /></div>
											</td>
										</logic:equal>
									</logic:iterate>
								</tr>
								<tr>
									<th><bean:message key="number_of_components" /></th>
									<logic:iterate id="repartitionId" name="repartition"
										indexId="counter" type="Double">
										<logic:greaterThan name="repartitionId" value="0">
											<logic:notEmpty name="form" property="parentId">
												<bean:define id="parentId" name="form" property="parentId"
													type="String" />
												<td>
												<div align="center"><A
													HREF='<%="mark.do?action=mark&projectId="
																    + projectId
																    + "&currentAuditId="
																    + currentAuditId
																    + "&previousAuditId="
																    + previousAuditId
																    + "&tre="
																    + treId
																    + "&factorParent="
																    + parentId
																    + "&currentMark="
																    + counter.intValue()%>'
													class="nobottom"> <%=(int) (repartitionId.doubleValue())%> </A></div>
												</td>
											</logic:notEmpty>
											<logic:empty name="form" property="parentId">
												<td>
												<div align="center"><A
													HREF='<%="mark.do?action=mark&projectId=" + projectId + "&currentAuditId=" + currentAuditId + "&previousAuditId=" + previousAuditId + "&tre=" + treId + "&currentMark=" + counter.intValue()%>'
													class="nobottom"> <%=(int) (repartitionId.doubleValue())%> </A></div>
												</td>
											</logic:empty>
										</logic:greaterThan>
										<logic:equal name="repartitionId" value="0">
											<td>
											<div align="center">0</div>
											</td>
										</logic:equal>
									</logic:iterate>
								</tr>
							</TBODY>
						</table>
						<br />
						<%-- 3- DISPLAY : Basic graph repartition --%>
						<bean:define id="imgBar" name="projectSummaryForm"
							property="barGraph.srcName" type="String" />
						<bean:define id="mapBar" name="projectSummaryForm"
							property="barGraph.useMapName" type="String" />
						<%=((GraphMaker) ((ProjectSummaryForm) (request.getSession().getAttribute("projectSummaryForm"))).getBarGraph()).getMapDescription()%>
						<img src="<%=imgBar%>" usemap="<%=mapBar%>" border="0" />
						<br />
						<%-- 4- DISPLAY : practice detail --%>
						<fieldset><legend><b><bean:message key="qualimetric_element.title" /></b></legend>
						<br />
						<jsp:include page="/jsp/results/project/information_common.jsp">
							<jsp:param name="expandedDescription" value="false" />
						</jsp:include> <br />
						</fieldset>
					</div>
				</af:tab>
				
				<%-- Second TAB : display bar chart repartition --%>
				<af:tab key="project.results.practice.graphes.detailed" name="barchart"
					lazyLoading="false"
					isTabSelected='<%=""+selectedTab.equals("barchart")%>'>
					<div style="background-color: white; padding: 10px">					
						<%-- dans le cas d'une r�partition par pas de 0,1 on affiche �galement l'histogramme --%>
						<logic:notEmpty name="projectSummaryForm" property="histoBarGraph">
							<bean:define id="img" name="projectSummaryForm"
								property="histoBarGraph.srcName" type="String" />
							<bean:define id="map" name="projectSummaryForm"
								property="histoBarGraph.useMapName" type="String" />
							<%-- ligne necessaire --%>
							<%=((GraphMaker) ((ProjectSummaryForm) (request.getSession().getAttribute("projectSummaryForm"))).getHistoBarGraph()).getMapDescription()%>
							<img src="<%=img%>" usemap="<%=map%>" border="0" />
						</logic:notEmpty>
					</div>
				</af:tab>
				
				<%-- Third TAB : display distribution map --%>
				<af:tab key="project.results.practice.graphes.distributionmap" name="dmap"
					lazyLoading="false"
					isTabSelected='<%=""+selectedTab.equals("dmap")%>'>
					<div style="background-color: white; padding: 10px; width: auto; height: auto; overflow: auto">
						<script type="text/javascript" language="javascript" src="gwt.dm/gwt.dm.nocache.js"></script>
						<div id="distributionmap"></div>
					</div>
				</af:tab>
				
			</af:tabbedPane>

		</af:canvasCenter>
	</af:body>
</af:page>