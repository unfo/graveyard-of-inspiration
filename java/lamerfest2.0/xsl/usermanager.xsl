<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
<xsl:output method="html"/>
<xsl:import href="lamerfest.xsl"/>

<!--

	This is what UserManagerServlet produces:

	<message>something good happened</message>
	<error>something bad occured</error>

	<users>
		<user>
			<userID></userID>
			<password></password>
			<realName></realName>
			<level></level>
		</user>
	</users>

	<ejbProcessingTime>150</ejbProcessingTime>

-->

<xsl:template match="application">

	<span class="textHeading">User manager</span><br/>

	Tältä sivulta voit lisätä, tuhota ja muokata Lamerfestin käyttäjiä.<br/><br/>
	<a href="usermanager?hablaa">Päivitä tiedot</a>

	<p>
		<xsl:apply-templates select="error"/>
		<xsl:apply-templates select="message"/>
	</p>

	<b>Lisää käyttäjä:</b>

	<form method="POST">
	<input type="hidden" name="action" value="add"/>
	<table>
		<tr>
			<td>userID</td>
			<td>salasana</td>
			<td>oikea nimi</td>
			<td>level</td>
		</tr>
		<tr>
			<td><input type="text" name="userID"/></td>
			<td><input type="text" name="password"/></td>
			<td><input type="text" name="realName"/></td>
			<td>
				<select name="level">
					<option value="0">Laama :P</option>
					<option value="10">Admin</option>
				</select>
			</td>
		</tr>
		<tr>
			<td></td><td></td><td></td>
			<td><input type="submit" value="lisää"/></td>
		</tr>
	</table>
	</form>

	<br/>
	<b>Muokkaa / tuhoa käyttäjiä</b>

	<table>
		<tr>
			<td>userID</td>
			<td>salasana</td>
			<td>oikea nimi</td>
			<td>level</td>
		</tr>
		<xsl:for-each select="users/user">
		<xsl:sort select="level" data-type="number" order="descending"/>
			<tr>
				<form method="POST">
					<input type="hidden" name="userID" value="{userID}"/>
					<td><xsl:value-of select="userID"/></td>
					<td><input type="text" name="password" value="{password}"/></td>
					<td><input type="text" name="realName" value="{realName}"/></td>
					<td>
						<select name="level">
							<xsl:choose>
								<xsl:when test="level='10'">
									<option value="0">Laama :P</option>
									<option value="10" selected="true">Admin</option>
								</xsl:when>
								<xsl:otherwise>
									<option value="0" selected="true">Laama :P</option>
									<option value="10">Admin</option>
								</xsl:otherwise>
							</xsl:choose>
						</select>
					</td>
					<td>
						<input type="submit" name="action" value="update"/>
						<input type="submit" name="action" value="delete"/>
					</td>
				</form>
			</tr>
		</xsl:for-each>
	</table>

	<div align="right">
		Pavut keitettiin ajassa <xsl:value-of select="ejbProcessingTime"/> ms
	</div>

</xsl:template>

<xsl:template match="error">
	<font color="red">
		<b>Error!</b>
		<xsl:text> </xsl:text>
		<xsl:value-of select="."/><br/>
	</font>
</xsl:template>

<xsl:template match="message">
	<font color="blue">
		<xsl:value-of select="."/><br/>
	</font>
</xsl:template>

</xsl:stylesheet>