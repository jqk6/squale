<%@ taglib uri="http://www.squale.org/welcom/tags-welcom" prefix="af" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>

<!-- Recovery of the index value -->
<% String indexValue = (String) request.getParameter( "index" );%>

<table class="formulaire" >
	 <thead>
		<tr>
			<th class="check_col">
				<div id="up<%=indexValue%>">
					<img src="theme/charte_v03_001/img/picto/blanc/arTr_U.gif" onclick="move('<%=indexValue %>','up')" style="cursor:pointer" />
				</div>
				<div id="down<%=indexValue%>">
					<img src="theme/charte_v03_001/img/picto/blanc/arTr_D.gif" onclick="move('<%=indexValue %>','down')" style="cursor:pointer" />
				</div>
				<input type="hidden" id="htmlText<%=indexValue%>" value="statId" >
			</th>
			<th id="title<%=indexValue%>" class="height_col" onclick="show('div_position<%=indexValue%>')" style="cursor:pointer">
				<bean:message key="homepage_management.stats.statsTitle" />
			</th>
		</tr>
	</thead>
</table>
<div id="div_position<%=indexValue%>">
	<table class="formulaire">
		<tbody id="body5">
			<tr>
				<td class="check_col">
					<html:checkbox property="statisticCheck" styleId = "statId" onclick="modifyTableOrder('statId')"/>
					<html:hidden property="positionStat" styleId="position_statId" value="<%=indexValue%>"></html:hidden>
				</td>
				<td>
					<bean:message key="homepage_management.stats.stats" />
				</td>
			</tr>
		</tbody>
	</table>
</div>