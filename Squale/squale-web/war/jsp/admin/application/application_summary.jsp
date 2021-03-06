<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@taglib uri="http://www.squale.org/welcom/tags-welcom" prefix="af"%>
<%@taglib uri="http://www.squale.org/squale/security" prefix="sec"%>

<%@ page import="org.squale.squaleweb.applicationlayer.formbean.creation.CreateProjectForm"%>
<%@ page import="org.squale.squalecommon.enterpriselayer.businessobject.profile.ProfileBO"%>
<%@ page import="org.squale.squalecommon.enterpriselayer.businessobject.component.AuditBO"%>
<%@ page import="org.squale.squaleweb.util.SqualeWebActionUtils"%>
<%@ page import="org.squale.squaleweb.resources.WebMessages"%>

<bean:define id="applicationId" name="createApplicationForm"
	property="applicationId" type="String" />
<bean:size name="createApplicationForm" property="rights" id="nbUsers" />

<%-- On va interdire l'ecriture pour les lecteurs --%>
<bean:define id="profile"
	name="<%=org.squale.welcom.struts.util.WConstants.USER_KEY%>"
	property="<%=\"profile(\"+applicationId+\")\"%>" />

<%
// On supprime tous les formulaires li�s � l'ajout d'un projet
CreateProjectForm projectForm = (CreateProjectForm) session.getAttribute("createProjectForm");
// On supprime tous les formulaires des t�ches
if (projectForm != null) {
    java.util.Iterator it = projectForm.getTaskForms().iterator();
    while (it.hasNext()) {
        session.removeAttribute((String) it.next());
    }
}
// Le formulaire du projet
session.removeAttribute("createProjectForm");
// L'attribut indiquant si on est en modification ou cr�ation de projet
session.removeAttribute("modification");
%>


<af:page accessKey="default">
	<af:body>
		<af:canvasCenter titleKey="application_creation.subtitle.config">
			<br/>
			<h2>
				<bean:message key="application.creation.field.application_name" /> 
				<bean:write scope="session" name="createApplicationForm" property="applicationName" />
			</h2>
			<div id="resultheader">
				<table cellpadding="2" cellspacing="0" border="0">
					<logic:equal name="nbUsers" value="0">
						<tr align="center">
							<td>-- <bean:message key="application_creation.config.archived" /> --</td>
						</tr>
					</logic:equal>
					<tr>
						<td><b><bean:message key="application_creation.config.site" /></b></td>
						<td><bean:write scope="session" name="createApplicationForm" property="serveurForm.name" /></td>
					</tr>
					<logic:equal scope="session" name="createApplicationForm" property="milestone" value="true">
						<tr>
							<td><b><bean:message key="audit.type_long" /></b></td>
							<td><bean:message key="audit.type.milestone" /></td>
						</tr>
					</logic:equal> 
					<logic:notEqual scope="session" name="createApplicationForm" property="milestone" value="true">
						<tr>
							<td><b><bean:message key="audit.type_long" /></b></td>
							<td><bean:message key="audit.type.normal" /></td>
						</tr>
						<tr>
							<td><b><bean:message key="application_creation.config.audit_frequency_short" /></b></td>
							<td><bean:write scope="session" name="createApplicationForm" property="auditFrequency" /></td>
						</tr>
					</logic:notEqual>
					<tr>
						<td><b><bean:message key="application_creation.config.purge_frequency" /></b></td>
						<td><bean:write scope="session" name="createApplicationForm" property="purgeDelay" /></td>
					</tr>
					<tr>
						<td colspan="2">
							<af:dropDownPanel titleKey="application_creation.config.information">
								<table>
									<tr>
										<td><b><bean:message key="application_creation.config.inProduction" /></b></td>
										<td>
											<html:checkbox property="isInProduction" name="createApplicationForm" disabled="true" />
										</td>
									</tr>
									<tr>
										<td><b><bean:message key="application_creation.config.externalDev" /></b></td>
										<td>
											<html:checkbox property="externalDev" name="createApplicationForm" disabled="true" />
										</td>
									</tr>
									<tr>
										<td><b><bean:message key="application_creation.config.qualityAtBeginning" /></b></td>
										<td>
											<html:checkbox property="qualityApproachOnStart" name="createApplicationForm" disabled="true" />
										</td>
									</tr>
								</table>
							</af:dropDownPanel>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<af:dropDownPanel titleKey="application_creation.config.cost">
								<table>
									<logic:equal scope="session" name="createApplicationForm" property="inInitialDev" value="true">
										<tr>
											<td><b><bean:message key="application_creation.config.initialGlobalCost" /></b></td>
											<td><bean:write scope="session" name="createApplicationForm"  property="globalCostInitial" /></td>
										</tr>
										<tr>
											<td><b><bean:message key="application_creation.config.initialDevCost" /></b></td>
											<td><bean:write scope="session" name="createApplicationForm"  property="devCost" /></td>
										</tr>
									</logic:equal> 
									<logic:notEqual scope="session" name="createApplicationForm" property="inInitialDev" value="true">
										
										<tr>
											<td><b><bean:message key="application_creation.config.maintenanceGlobalCost" /></b></td>
											<td><bean:write scope="session" name="createApplicationForm" property="globalCostMaintenance" /></td>
										</tr>
									</logic:notEqual>
								</table>
							</af:dropDownPanel>
						</td>
					</tr>
				</table>
			</div>
			<sec:ifHasProfile profiles="<%=ProfileBO.ADMIN_PROFILE_NAME + ',' + ProfileBO.MANAGER_PROFILE_NAME%>" applicationId="<%=applicationId%>">
				<af:buttonBar>
					<logic:greaterThan name="nbUsers" value="0">
						<af:button name="supprimer" messageConfirmationKey="application_purge.confirm"
							onclick='<%="manageApplication.do?action=deleteConfirm&applicationId=" + applicationId%>' />
					</logic:greaterThan>
					<af:button name="physicallyRemove" messageConfirmationKey="application_purge.confirm"
						accessKey="admin" onclick='<%="manageApplication.do?action=purgeConfirm&applicationId=" + applicationId%>' />
					<af:button name="modify.configuration"
						onclick='<%="utilLink.do?action=configApplication&applicationId=" + applicationId + "&modification=true"%>' />
				</af:buttonBar>
			</sec:ifHasProfile>				
			<br/>
			<sec:ifNotHasProfile profiles="<%=ProfileBO.ADMIN_PROFILE_NAME + ',' + ProfileBO.MANAGER_PROFILE_NAME%>" applicationId="<%=applicationId%>">
				<br/>
				<div style="color: #f00">
					<bean:message key="page.readonly" />
				</div>
				<br/>
			</sec:ifNotHasProfile>
			<br/>
			<%-- affichage des sections de programmation d'audits --%>
			<bean:define id="status" name="createApplicationForm" property="status" scope="session" />
			<%Integer statusValidated = new Integer(org.squale.squalecommon.enterpriselayer.businessobject.component.ApplicationBO.VALIDATED);%>
			<%-- affichage de la section de programmation d'un audit de jalon --%>
			<%-- cette section est disponible aux administrateurs et aux gestionnaires de l'application si celle-ci est valid�e --%>
			<%--rem : la "complexit�" du test ne permet pas d'utiliser <sec:ifHasProfile> --%>
			<%if (profile.equals(ProfileBO.ADMIN_PROFILE_NAME) || (profile.equals(ProfileBO.MANAGER_PROFILE_NAME) && status.equals(statusValidated))) {%>
			<div class="frame_border">
				<bean:define name="createApplicationForm" property="milestoneAudit" id="milestoneAuditForm" />
				<h2>
					<bean:message key="application.audit.milestone.add" />
				</h2>
				<div style="color: #f00">
					<html:messages id="msg" message="true" property="milestoneMsg">
						<bean:write name="msg" />
						<br>
					</html:messages>
				</div>
				<af:dropDownPanel titleKey="application.audit.label.help">
					<div>
						<bean:message key="application.audit.label.help.details" />
					</div>
				</af:dropDownPanel>
				<af:form action="add_milestone.do">
					<input type="hidden" name="applicationId" value="<%=applicationId%>" />
					<input type="hidden" name="type" value="<%=AuditBO.MILESTONE%>" />
					<table width="100%" class="formulaire" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<af:field key="application.audit.label.name"
								name="milestoneAuditForm" property="name" size="80"
								styleClassLabel="td1" isRequired="true" type="TEXT" />
						</tr>
						<tr>
							<logic:empty name="milestoneAuditForm" property="historicalDate">
								<af:field key="application.audit.label.date"
									name="milestoneAuditForm" property="historicalDate" size="40"
									type="DATE" dateFormatKey="date.format.simple"
									value='<%=SqualeWebActionUtils.getTodayDate(request.getLocale(), "date.format.simple")%>'
									isRequired="true" styleClassLabel="td1" />
							</logic:empty>
							<logic:notEmpty name="milestoneAuditForm"
								property="historicalDate">
								<af:field key="application.audit.label.date"
									name="milestoneAuditForm" property="historicalDate" size="40"
									type="DATE" dateFormatKey="date.format.simple" isRequired="true"
									styleClassLabel="td1" />
							</logic:notEmpty>
						</tr>
					</table>
					<af:buttonBar>
						<%-- Si un audit de jalon n'existe pas, on lui propose d'en cr�er un --%>
						<logic:equal name="milestoneAuditForm" property="name" value="">
							<af:button callMethod="addMilestone" name="valider" type="form"
								messageConfirmationKey="audit_milestone.confirm" />
						</logic:equal>
						<logic:notEqual name="milestoneAuditForm" property="name" value="">
							<%-- Sinon on peut le modifier ou le supprimer --%>
							<af:button callMethod="modifyMilestone" name="modify" type="form" />
							<af:button callMethod="deleteMilestone" name="supprimer"
								type="form"
								messageConfirmationKey="audit_milestone.delete.confirm" />
						</logic:notEqual>
					</af:buttonBar>
				</af:form>
	
				<%-- affichage de la section de programmation d'un audit de suivi --%>
				<%-- cette section est affich�e pour les applications en audits de suivi --%>
				<logic:equal name="createApplicationForm" property="milestone"
					scope="session" value="false">
					<h2><bean:message key="application.audit.branch.add" /></h2>
					<af:dropDownPanel titleKey="buttonTag.menu.aide">
						<bean:message key="application.audit.branch.add.help" />
					</af:dropDownPanel>
					<br />
					<div style="color: #f00"><html:messages id="msg"
						message="true" property="branchMsg">
						<bean:write name="msg" />
						<br>
					</html:messages></div>
					<af:form action="add_branch.do">
						<input type="hidden" name="applicationId"
							value="<%=applicationId%>" />
						<input type="hidden" name="type" value="<%=AuditBO.NORMAL%>" />
						<table width="100%" class="formulaire" cellpadding="0"
							cellspacing="0" border="0">
							<%-- la date d'ex�cution de l'audit est (arbitrairement) v�hicul�e 
						par la propri�t� 'realBeginningDate' --%>
							<af:field key="application.audit.branch.date" name="auditForm2"
								property="date" type="DATE" dateFormatKey="date.format.simple"
								isRequired="true" styleClassLabel="td1" width="75%" />
						</table>
						<af:buttonBar>
							<af:button callMethod="addBranch" name="valider" type="form"
								messageConfirmationKey="audit_branch.confirm" />
						</af:buttonBar>
					</af:form>
				</logic:equal>
			</div>
			<%}%>

			<br />
			<br/>
			<div class="frame_border">
				<h2>
					<bean:message key="applications_creation.list.projects.title" />
				</h2>
				<af:dropDownPanel titleKey="buttonTag.menu.aide">
					<bean:message key="applications_creation.list.projects.details" />
				</af:dropDownPanel>
				<br />
				<div>
					<html:messages id="msg" message="true" property="projectsMessages">
						<b><bean:write name="msg" /></b>
						<br/>
					</html:messages>
				</div>
				<div style="color: #f00">
					<html:errors property="invalid.selection" />
				</div>
				<af:form action="manageApplication.do">
					<input type="hidden"
						name="<%=org.squale.squaleweb.applicationlayer.action.accessRights.BaseDispatchAction.DO_NOT_RESET_FORM%>"
						value="true" />
					<af:table name="createApplicationForm"
						emptyKey="application_creation.list.project.empty" pageLength="30"
						property="projects">
						<af:cols id="project">
							<!-- on n'affiche les checkboxes que si l'utilisateur est admin ou manager -->
							<sec:ifHasProfile profiles="<%=ProfileBO.ADMIN_PROFILE_NAME + ',' + ProfileBO.MANAGER_PROFILE_NAME%>" applicationId="<%=applicationId%>">
								<af:colSelect />
							</sec:ifHasProfile>
							<!-- Dans ce c�s l� il faut passer � la fois l'id du projet et de l'application car on va modifier
							la conf d'un projet en v�rifiant que l'utilisateur � les droits sur l'appli -->
							<af:col property="projectName"
								key="project_creation.field.project_name" sortable="true"
								href='<%="config_project.do?action=selectProjectToModify&applicationId=" + applicationId%>'
								paramName="project" paramId="projectId" paramProperty="projectId">
							</af:col>
							<af:col property="sourceManagement"
								key="project_creation.field.source_management" sortable="true">
							</af:col>
							<af:col property="profile"
								key="project_creation.field.project_profile" sortable="true">
							</af:col>
							<af:col property="status" key="project.status" sortable="true">
								<bean:message
									key='<%="project.status_" + ((CreateProjectForm) project).getStatus()%>' />
							</af:col>
						</af:cols>
					</af:table>
					<sec:ifHasProfile profiles="<%=ProfileBO.ADMIN_PROFILE_NAME + ',' + ProfileBO.MANAGER_PROFILE_NAME%>" applicationId="<%=applicationId%>">
						<af:buttonBar>
							<af:button name="add.project"
								onclick="<%=\"location.href='config_project.do?action=newProject&applicationId=\"+applicationId+\"'\"%>" />
							<af:button name="delete" toolTipKey="toolTip.delete.projects"
								callMethod='deleteProjects'
								messageConfirmationKey="delete.projects.confirm"
								accessKey="admin" />
							<af:button name="disactivate"
								toolTipKey="toolTip.disactivate.projects"
								callMethod='disactiveOrReactiveProjects' />
						</af:buttonBar>
					</sec:ifHasProfile>
				</af:form>
			</div>
			<br />
			<%-- Affichage pour les administrateur SQUALE uniquement --%>
			<sec:ifHasProfile profiles="<%=ProfileBO.ADMIN_PROFILE_NAME%>" applicationId="<%=applicationId%>">
				<bean:size name="createApplicationForm"
					property="accessListForm.list" id="nbAccesses" />
				<logic:greaterThan name="nbAccesses" value="0">
					<fieldset><legend> <b><bean:message
						key="last.user.accesses.title"
						arg0='<%=WebMessages.getString(request, "application.max.accesses")%>' /></b>
					</legend>
					<table>
						<logic:iterate name="createApplicationForm"
							property="accessListForm.list" id="accessForm">
							<bean:define name="accessForm" property="matricule"
								id="userMatricule" type="String" />
							<bean:define name="accessForm" property="date" id="userDate"
								type="java.util.Date" />
							<tr>
								<td><bean:message key="last.user.accesses.content"
									arg0="<%=userMatricule%>"
									arg1="<%=SqualeWebActionUtils.getFormattedDate(userDate, request.getLocale())%>" />
								</td>
							</tr>
						</logic:iterate>
					</table>
					</fieldset>
				</logic:greaterThan>
			</sec:ifHasProfile>
			<br />
		</af:canvasCenter>
	</af:body>
</af:page>