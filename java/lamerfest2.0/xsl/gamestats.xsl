<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="staticFileName"/>

<xsl:output method="html"/>
<xsl:import href="lamerfest.xsl"/>

<xsl:variable name="staticContent" select="document(concat('../webroot/', $staticFileName))"/>

<xsl:template match="application">

	<span class="textHeading">Pelistatistiikkaa</span>

	<br/><br/>

	<xsl:apply-templates select="$staticContent/stats"/>

</xsl:template>

<xsl:template match="stats">
	
	<xsl:apply-templates select="servers/server"/>

</xsl:template>

<xsl:template match="server">

	<table cellpadding="2" cellspacing="1" border="0" bgcolor="#666666">
		<tr>
			<td bgcolor="#CECECE" colspan="5"><b><xsl:value-of select="name"/></b></td>
		</tr>
		<tr>
			<td bgcolor="#DEDEDE">
				<b>osoite</b>
			</td>
			<td bgcolor="#DEDEDE">
				<b>peli</b>
			</td>
			<td bgcolor="#DEDEDE">
				<b>pelityyppi</b>
			</td>
			<td bgcolor="#DEDEDE">
				<b>kartta</b>
			</td>
			<td bgcolor="#DEDEDE">
				<b>pelaajia</b>
			</td>
		</tr>
		<tr>
			<td bgcolor="#FFFFFF"><xsl:value-of select="@ip"/></td>
			<td bgcolor="#FFFFFF"><xsl:value-of select="type"/></td>
			<td bgcolor="#FFFFFF"><xsl:value-of select="game"/></td>
			<td bgcolor="#FFFFFF"><xsl:value-of select="map"/></td>
			<td bgcolor="#FFFFFF">
				<xsl:value-of select="players/@currently"/>
				<xsl:text> </xsl:text>/ max.<xsl:text> </xsl:text>
				<xsl:value-of select="players/@max"/>
			</td>
		</tr>
		<tr>
		<td colspan="5" bgcolor="#FFFFFF">
			<table width="100%" cellpadding="2">
				<tr>
					<td><b>nimi</b></td>
					<td><b>pelannut</b></td>
					<td><b>fragit</b></td>
				</tr>
				<xsl:apply-templates select="players/player">
					<xsl:sort data-type="number" select="@frags" order="descending"/>
				</xsl:apply-templates>
			</table>
		</td>
		</tr>
	</table>

</xsl:template>

<xsl:template match="player">
	<tr>
		<td><xsl:value-of select="name"/></td>
		<td><xsl:value-of select="time"/></td>
		<td><xsl:value-of select="@frags"/></td>
	</tr>
</xsl:template>

</xsl:stylesheet>