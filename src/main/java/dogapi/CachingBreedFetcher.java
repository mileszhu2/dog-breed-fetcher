package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private final HashMap<String, List<String>> results = new HashMap<>();
    private final BreedFetcher breedFetcher;
    public CachingBreedFetcher() {this.breedFetcher = new DogApiBreedFetcher();}
    public CachingBreedFetcher(BreedFetcher fetcher) {
        if (fetcher instanceof CachingBreedFetcher) {
            this.breedFetcher = new DogApiBreedFetcher();
        }
        else {
            this.breedFetcher = fetcher;
        }
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        if (results.containsKey(breed)) {
            return results.get(breed);
        }
        else {
            callsMade++;
            try {
                List<String> result = breedFetcher.getSubBreeds(breed);
                results.put(breed, result);
                return result;
            }
            catch (BreedNotFoundException e) {System.out.println(e);}
        }
        return List.of("error");
    }

    public int getCallsMade() {
        return callsMade;
    }
}