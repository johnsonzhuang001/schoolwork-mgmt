const BASE_URL = process.env.REACT_APP_API_URL ?? "https://localhost:9443";

export async function POST(request: Request) {
  const { runId, teamUrl, callbackUrl } = await request.json();
  if (!runId || !teamUrl || !callbackUrl) {
    return new Response(`runId, teamUrl, callbackUrl are required`, {
      status: 400,
    });
  }
  try {
    const result = await fetch(`${BASE_URL}/evaluate`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        runId,
        teamUrl,
        callbackUrl,
      }),
    });
    return Response.json(result);
  } catch (error) {
    if (error instanceof Error) {
      return new Response(`Failed to get evaluation: ${error.message}`, {
        status: 500,
      });
    }
  }
}
