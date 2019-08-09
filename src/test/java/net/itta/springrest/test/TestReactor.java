
package net.itta.springrest.test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Test;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;


public class TestReactor {
    
    @Test
    public void vide() throws InterruptedException{
        Mono<String> monovide = Mono.empty();
        assertFalse("il ne devrait pas exister", monovide.blockOptional(Duration.ofMillis(10)).isPresent());
        
        Mono<String> monopasvide = Mono.just("salut");
        monopasvide.blockOptional(Duration.ofMillis(10)).ifPresent(s->assertEquals("pas de salut", s,"salut"));
        
        
        Flux<String> fluxvide = Flux.empty();
        assertNull("devrait être = null",fluxvide.blockFirst(Duration.ofMillis(10)));
        
        Flux<String> fluxpasvide = Flux.just("salut", "monde");
        Iterator<String> is=   fluxpasvide.toIterable().iterator();
        assertEquals("salut",is.next());
        assertEquals("monde",is.next());
        assertFalse(is.hasNext());
        
        Flux<String>  fluxpasvide2 = Flux.just("salut", "monde");
        //fluxpasvide2.subscribe( System.out::println); //pas testable
        
        Flux<Character> alphabet = Flux.range(65, 26).map(i->(char)(int)i);
        
        Flux<Character>  chars= Flux.just("salut", "monde")
                .flatMap(s-> Flux.fromArray(s.toUpperCase().split("")))
//                .distinct()
//                .sort()
                .doOnRequest(l->System.out.println(l) )
                .map(s->s.charAt(0));
        chars.subscribe(c-> System.out.print(c));
         
        Flux.concat(chars, alphabet).subscribe(c-> System.out.print(c));
        System.out.println("");
        
        Flux.merge(chars, alphabet).subscribe(c-> System.out.print(c));
        System.out.println("");
        alphabet.zipWith(chars, (i,j)-> i+"-"+j+", ").subscribe(c-> System.out.print(c));   
        System.out.println("");
        alphabet.mergeWith(chars).subscribe(c-> System.out.print(c));
        System.out.println("");
        Flux.range(65, 26).map(i->(char)(int)i)
                .parallel(4)
                .runOn(Schedulers.elastic())
                .doOnSubscribe(s-> System.out.println("Appelé"))
                .subscribe(c-> System.out.print(c));
       Thread.sleep(1000);
        System.out.println("");
        Flux.range(65, 26).map(i->(char)(int)i)
                .mergeWith(Flux.range(97, 26).map(i->(char)(int)i))
                .parallel(4)
                .runOn(Schedulers.elastic())
                
                .sequential()
                .subscribe(c-> System.out.print(c));
Thread.sleep(1000);
        System.out.println("");
    }
    
    
    
}
