package guestbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//‎ (｡◕‿◕｡)
// Durch die @Controller Annotation wird Spring mitgeteilt, dass es sich um einen Controller handelt, welcher Webrequest behandeln kann.
// Des Weiteren durchsucht Spring die Klasse nach @RequestMapping Methoden. 
// Die index() Methode wird aufgerufen, wenn die "/" Url aufgerufen wird.
// Der Rückgabewert ist der Name einer .html-Datei, welche sich im /src/main/webapp/templates-Verzeichnis befindet.
// Über den Rückgabebewert wird bestimmt, welche Seite nach dem Aufruf an den Browser geschickt wird.

@Controller
public class WelcomeController {

	//‎ (｡◕‿◕｡)
	// Der Name der Methode ist egal, wichtig ist das Mapping über die Annotation sowie der Rückgabewert und -typ.
	@RequestMapping("/")
	public String index() {
		return "index";
	}
}
