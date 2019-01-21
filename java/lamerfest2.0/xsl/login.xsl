<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
<xsl:output method="html"/>
<xsl:import href="lamerfest.xsl"/>

<xsl:template match="application">

	<form method="POST">

		<input type="hidden" name="action" value="login"/>
		<xsl:if test="targetURL">
			<input type="hidden" name="targetURL" value="{targetURL}"/>
		</xsl:if>

		<xsl:if test="error">
			<font color="#FF0000">
				<b>Virhe!</b><br/>
				<xsl:for-each select="error">
					<xsl:value-of select="."/><br/>
				</xsl:for-each>
			</font>
		</xsl:if>

		<br/>
		Tunnus:<br/>
		<input type="text" name="user" value="{user}"/><br/>
		Salasana:<br/>
		<input type="password" name="pwd" value="{pwd}"/><br/>

		<br/>

		<input type="submit" value="loggaa sisään"/>

	</form>

</xsl:template>

</xsl:stylesheet>