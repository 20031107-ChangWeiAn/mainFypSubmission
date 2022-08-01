
package fyp;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // indicate to Spring that this class should be instantiated, and any
				// dependencies should be injected
public class WorldBayConfig implements WebMvcConfigurer {
	@Override // the child class method overrides the base class method
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// configure /uploads folder as a resource location
		String dirName = "uploads/";
		// get the directory path of uploads folder

		Path uploadDir = Paths.get(dirName);
		String uploadPath = uploadDir.toFile().getAbsolutePath();
		// add this file path as the resource location
		registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + uploadPath + "/");
	}
}
