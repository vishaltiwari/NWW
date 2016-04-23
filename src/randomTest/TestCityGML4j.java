package randomTest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.CityGMLBuilder;
import org.citygml4j.model.citygml.appearance.Appearance;
import org.citygml4j.model.citygml.appearance.AppearanceProperty;
import org.citygml4j.model.citygml.appearance.Color;
import org.citygml4j.model.citygml.appearance.SurfaceDataProperty;
import org.citygml4j.model.citygml.appearance.X3DMaterial;
import org.citygml4j.model.citygml.building.AbstractBoundarySurface;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.model.gml.geometry.aggregates.MultiSurface;
import org.citygml4j.model.module.citygml.CityGMLVersion;
import org.citygml4j.model.module.citygml.CoreModule;
import org.citygml4j.util.gmlid.DefaultGMLIdManager;
import org.citygml4j.util.gmlid.GMLIdManager;
import org.citygml4j.util.walker.FeatureWalker;
import org.citygml4j.xml.io.CityGMLInputFactory;
import org.citygml4j.xml.io.CityGMLOutputFactory;
import org.citygml4j.xml.io.reader.CityGMLReader;
import org.citygml4j.xml.io.writer.CityGMLWriter;

public class TestCityGML4j {

	public static void main(String[] args) throws Exception {
		final SimpleDateFormat df = new SimpleDateFormat("[HH:mm:ss] "); 

		System.out.println(df.format(new Date()) + "setting up citygml4j context and JAXB builder");
		CityGMLContext ctx = new CityGMLContext();
		CityGMLBuilder builder = ctx.createCityGMLBuilder();

		System.out.println(df.format(new Date()) + "reading CityGML file LOD2_Building_v100.gml");
		CityGMLInputFactory in = builder.createCityGMLInputFactory();
		CityGMLReader reader = in.createCityGMLReader(new File("/home/vishal/software/citygml4j/samples/datasets/LOD2_Building_v100.gml"));
		CityModel cityModel = (CityModel)reader.nextFeature();
		reader.close();

		final GMLIdManager gmlIdManager = DefaultGMLIdManager.getInstance();

		System.out.println(df.format(new Date()) + "using FeatureWalker to walk through document and to process boundary surface features");
		FeatureWalker walker = new FeatureWalker() {

			@Override
			public void visit(AbstractBoundarySurface boundarySurface) {
				System.out.println(df.format(new Date()) + "adding X3DMaterial information to " + boundarySurface.getId());

				MultiSurface multiSurface = boundarySurface.getLod2MultiSurface().getMultiSurface();
				String id = multiSurface.getId();
				if (id == null || id.length() == 0) {
					id = gmlIdManager.generateUUID();
					multiSurface.setId(id);
				}

				Double red, green, blue;
				switch (boundarySurface.getCityGMLClass()) {
				case BUILDING_ROOF_SURFACE:
					System.out.println("This is a Roof with ID"+boundarySurface.getId());
					//boundrySurface.
					red = 1.0; green = 0.0; blue = 0.0;
					break;
				case BUILDING_WALL_SURFACE:
					System.out.println("This is a Wall with ID"+boundarySurface.getId());
					red = 0.5; green = 0.5; blue = 0.5;
					break;
				default:
					System.out.println("This is a GroundSurface with ID"+boundarySurface.getId());
					red = 0.3; green = 0.3; blue = 0.3;
				}

				X3DMaterial material = new X3DMaterial();
				material.setDiffuseColor(new Color(red, green, blue));
				material.addTarget('#' + id);

				Appearance appearance = new Appearance();
				appearance.setTheme("rgbColor");
				appearance.addSurfaceDataMember(new SurfaceDataProperty(material));

				boundarySurface.addAppearance(new AppearanceProperty(appearance));
				super.visit(boundarySurface);
			}

		};

		cityModel.accept(walker);

		System.out.println(df.format(new Date()) + "writing citygml4j object tree as CityGML 2.0.0 document");
		CityGMLOutputFactory out = builder.createCityGMLOutputFactory(CityGMLVersion.v2_0_0);

		CityGMLWriter writer = out.createCityGMLWriter(new File("LOD2_Building_colorized_v200.gml"));
		writer.setPrefixes(CityGMLVersion.v2_0_0);
		writer.setDefaultNamespace(CoreModule.v2_0_0);
		writer.setSchemaLocations(CityGMLVersion.v2_0_0);
		writer.setIndentString("  ");

		writer.write(cityModel);

		writer.close();
		System.out.println(df.format(new Date()) + "CityGML file LOD2_Building_colorized_v200.gml written");
		System.out.println(df.format(new Date()) + "sample citygml4j application successfully finished");
	}

}
