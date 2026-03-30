const url = 'http://localhost:8080/api/activities';
const totalRequests = 10000;
const concurrencyLimit = 500;

async function runTest() {
    console.log(`🚀 Starting Scaled Stress Test: ${totalRequests} total requests...`);
    console.log(`📡 Concurrency limit: ${concurrencyLimit} simultaneous workers.`);
    const startTime = Date.now();
    let completed = 0;
    const results = { success: 0, errors: 0, latencies: [] };

    // Helper to run a single request
    async function executeRequest(id) {
        const userId = `user-${id.toString().padStart(5, '0')}`;
        const requestStart = Date.now();
        try {
            const res = await fetch(url, { headers: { 'X-User-ID': userId } });
            const requestEnd = Date.now();
            if (res.status === 200) {
                results.success++;
                results.latencies.push(requestEnd - requestStart);
            } else {
                results.errors++;
            }
        } catch (err) {
            results.errors++;
        }
        completed++;
        if (completed % 1000 === 0) {
            console.log(`🔄 Completed ${completed}/${totalRequests} requests...`);
        }
    }

    // Native Throttling with a Set of active Promises
    const active = new Set();
    for (let i = 1; i <= totalRequests; i++) {
        // If we reach the limit, wait for at least one to finish
        if (active.size >= concurrencyLimit) {
            await Promise.race(active);
        }
        
        const promise = executeRequest(i).finally(() => active.delete(promise));
        active.add(promise);
    }
    
    // Wait for the final batch
    await Promise.all(active);
    const endTime = Date.now();

    const avgLatency = results.latencies.reduce((a, b) => a + b, 0) / (results.latencies.length || 1);
    const maxLatency = Math.max(...(results.latencies.length ? results.latencies : [0]));
    const totalDuration = (endTime - startTime) / 1000;
    const rps = (totalRequests / totalDuration).toFixed(2);

    console.log('\n--- SCALED STRESS TEST REPORT ---');
    console.log(`Total Requests: ${totalRequests}`);
    console.log(`✅ Success: ${results.success}`);
    console.log(`❌ Errors:  ${results.errors}`);
    console.log(`🕒 Total Time: ${totalDuration.toFixed(2)} seconds`);
    console.log(`🚀 Throughput: ${rps} requests/second`);
    console.log(`📊 Avg Latency: ${avgLatency.toFixed(2)} ms`);
    console.log(`📈 Max Latency: ${maxLatency.toFixed(2)} ms`);
    console.log('---------------------------------');
}

runTest();
