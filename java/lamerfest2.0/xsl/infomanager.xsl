<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
<xsl:output method="html"/>
<xsl:import href="lamerfest.xsl"/>

<!--

	This is what InfoManagerServlet produces:

	<message>something good happened</message>
	<error>something bad occured</error>

	<items>
		<item>
			<id></id>
			<heading></heading>
		</item>
	</items>

	<ejbProcessingTime>150</ejbProcessingTime>

-->

<xsl:template match="application">

	<span class="textHeading">Uutishallinta</span><br/>

	Tältä sivulta voit lisätä ja tuhota uutisia kantaan/kannasta.<br/><br/>
	<a href="/infomanager?hablaa">Päivitä tiedot</a>

	<p>
		<xsl:apply-templates select="error"/>
		<xsl:apply-templates select="message"/>
	</p>

	<b>Lisää uutinen:</b>

	<form method="POST">
	<input type="hidden" name="action" value="add"/>
	Otsikko:<br/>
	<input type="text" name="heading"/><br/>
	Sisältö:<br/>
	<textarea name="content" cols="50" rows="5"></textarea>
	<br/>
	<input type="submit" value="lisää"/>
	</form>

	<br/><br/>
	<b>Kaikki uutiset</b>

	<table width="100%">
		<tr>
			<td><b>id</b></td>
			<td><b>otsikko</b></td>
		</tr>
		<xsl:for-each select="items/item">
			<tr>
				<td><xsl:value-of select="id"/></td>
				<td><xsl:value-of select="heading"/></td>
				<form method="POST">
					<input type="hidden" name="id" value="{id}"/>
					<td>
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

