import hw1.LRUCacheService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestCache {

    private LRUCacheService<Integer> cacheService = new LRUCacheService<>(3);

    @Before
    public void clearCache() {
        cacheService.clearCache();
    }

    @Test
    public void testPutOneElement() {
        Assert.assertFalse(cacheService.put(1, null).isPresent());

        cacheService.put(1, 1);

        Assert.assertTrue(cacheService.get(1).isPresent());
        Assert.assertEquals(1, (int) cacheService.get(1).get());
        Assert.assertEquals(1, cacheService.size());

    }

    @Test
    public void testPutOneElementAndDelete() {
        cacheService.put(1, 1);
        cacheService.delete(1);

        Assert.assertFalse(cacheService.get(1).isPresent());
        Assert.assertEquals(0, cacheService.size());
    }

    @Test
    public void testPutSeveralElements() {
        cacheService.put(1, 1);
        cacheService.put(2, 2);
        cacheService.put(3, 3);
        cacheService.put(2, 4);

        Assert.assertTrue(cacheService.get(1).isPresent());
        Assert.assertTrue(cacheService.get(2).isPresent());
        Assert.assertTrue(cacheService.get(3).isPresent());

        Assert.assertEquals(4, (int) cacheService.get(2).get());

        cacheService.delete(2);

        Assert.assertEquals(2, cacheService.size());

        cacheService.delete(3);

        Assert.assertEquals(1, cacheService.size());

        cacheService.put(2, 2);
        cacheService.put(3, 3);
        cacheService.put(4, 4);

        Assert.assertFalse(cacheService.get(1).isPresent());

        Assert.assertEquals(3, cacheService.size());

    }

    @Test
    public void testDelete() {
        Assert.assertFalse(cacheService.delete(1).isPresent());

        cacheService.put(1, 1);
        cacheService.put(2, 1);

        Assert.assertTrue(cacheService.delete(1).isPresent());

        Assert.assertEquals(1, cacheService.size());

        cacheService.put(1, 1);

        Assert.assertTrue(cacheService.delete(1).isPresent());

        Assert.assertEquals(1, cacheService.size());

        cacheService.clearCache();

        cacheService.put(1, 1);
        cacheService.put(2, 2);
        cacheService.put(3, 3);

        Assert.assertTrue(cacheService.delete(2).isPresent());

        Assert.assertEquals(2, cacheService.size());
    }

}
