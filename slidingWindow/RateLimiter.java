package slidingWindow;

import java.util.LinkedList;

public class RateLimiter {
    private int maxRequests;
    private long windowSize;
    private LinkedList<Long> requestTimes;

    public RateLimiter(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSize = windowSizeMillis;
        this.requestTimes = new LinkedList<>();
    }

    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        // Remove expired requests from the sliding window
        while (!requestTimes.isEmpty() && currentTime - requestTimes.getFirst() >= windowSize) {
            requestTimes.removeFirst();
        }

        // If the number of requests in the sliding window is less than the limit, allow
        // the request
        if (requestTimes.size() < maxRequests) {
            requestTimes.addLast(currentTime);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        RateLimiter rateLimiter = new RateLimiter(5, 10000); // Allow 5 requests per 10 seconds

        for (int i = 0; i < 10; i++) {
            boolean allowed = rateLimiter.allowRequest();
            if (allowed) {
                System.out.println("Request " + (i + 1) + ": Allowed");
            } else {
                System.out.println("Request " + (i + 1) + ": Blocked");
            }

        }
    }
}
