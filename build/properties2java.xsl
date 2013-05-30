<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="package" />

	<xsl:output method="text" />

	<xsl:template match="/">
		<xsl:text>package </xsl:text>
		<xsl:value-of select="$package" />
		<xsl:text>;</xsl:text>
		<xsl:text>&#10;</xsl:text>
		<xsl:text>&#10;</xsl:text>
		<xsl:text>public class BuildProperties {</xsl:text>
		<xsl:text>&#10;</xsl:text>
		<xsl:apply-templates select="//property" />
		<xsl:text>}</xsl:text>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>

	<xsl:template match="property">
		<xsl:text>public static final String </xsl:text>
		<xsl:value-of
			select="translate(substring-after(@name,'.'),'abcdefghijklmnopqrstuvwxyz.','ABCDEFGHIJKLMNOPQRSTUVWXYZ_')" />
		<xsl:text> = "</xsl:text>
		<xsl:value-of select="@value" />
		<xsl:text>";</xsl:text>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>

</xsl:stylesheet>