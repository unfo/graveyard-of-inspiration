<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:import href="lamerfest.xsl"/>
<xsl:output method="html"/>

<xsl:template match="application">

	<span class="textHeading">Lamerfest 2.0 jo lähes paketissa!</span><br/>
	<br/>
	Vielä pari tuntia iloa ja sitten jäämme odottamaan
	mitä ensi vuodeksi keksitään.
	<!--Lamerfest on täällä ja syövyttää mielenne!<br/>
	Seuraavaksi ohjelmassa on koneiden kasausta ja helpdeskin ajamista hulluksi &lt;evilgrin/&gt;<br/>
	<br/>
	Pitäkää hauskaa...... or else The Great Llama will come and take you by force<br/>
	<br/>
	<b><i>"Siis te ootte ihan henkisesti sairaita"</i></b><br/>
	-Tuntematon--><br/>

	<br/>
	<span class="textHeading">Sponsorit</span>
	<br/><br/>
	Lämpimät kiitokset kaikille sponsoreille, ilman heitä
	Lamerfest 2.0 ei olisi ollut mahdollinen.
	<br/>

	<img src="/kuvat/lame-sponssit.gif"/>

	<br/><br/>
	<span class="textHeading">Uutiset</span>
	<br/><br/>

	<table cellpadding="4" cellspacing="1" bgcolor="#666666">
	<xsl:for-each select="items/item">
		<tr valign="top"><td bgcolor="#CECECE">
			<b><xsl:value-of select="heading"/></b>
		</td>
		<td bgcolor="#ffffff">
			<xsl:value-of select="content" disable-output-escaping="yes"/>
		</td></tr>
	</xsl:for-each>
	</table>

</xsl:template>

</xsl:stylesheet>

