<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:tools="http://schemas.android.com/tools">

	<xsl:output indent="yes" />

	<xsl:template match="/">
		<resources>
			<xsl:apply-templates select="//property" />
		</resources>
	</xsl:template>

	<xsl:template match="property">
		<string tools:ignore="MissingTranslation"
			name="build_properties_{translate(substring-after(@name,'.'),'.','_')}">
			<xsl:value-of select="translate(@value,'@','')" />
		</string>
	</xsl:template>

</xsl:stylesheet>