package commons.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

@Converter(autoApply = true)
public class ColorConverter implements AttributeConverter<Color, String> {

    private final int six = 6;

    Logger log = LogManager.getLogger(this.getClass().getName());

    @Override
    public String convertToDatabaseColumn(Color attribute) {
        String hex = "#" + Integer.toHexString(attribute.getRGB()).substring(0, six);
        log.info("Convert " + attribute + " to " + hex);
        return hex;
    }

    @Override
    public Color convertToEntityAttribute(String dbData) {
        Color color = Color.decode(dbData);
        log.info("Convert " + dbData + " to " + color);
        return color;
    }
}
