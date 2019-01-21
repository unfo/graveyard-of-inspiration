<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="staticFileName"/>

<xsl:output method="html"/>
<xsl:import href="lamerfest.xsl"/>

<xsl:variable name="staticContent" select="document(concat('../webroot/', $staticFileName))"/>

<xsl:template match="application">

	<xsl:apply-templates select="$staticContent/static"/>

</xsl:template>

<xsl:template match="static">
	<xsl:apply-templates select="*|@*|text()" mode="content-copy"/>
</xsl:template>

<xsl:template match="*|@*|text()" mode="content-copy">
	<xsl:copy><xsl:apply-templates select="*|@*|text()" mode="content-copy"/></xsl:copy>
</xsl:template>

</xsl:stylesheet>