import { API_BASE_URL } from "@/constant/url";

export async function POST(request: Request) {
  const { runId, teamUrl, callbackUrl } = await request.json();
  if (!runId || !teamUrl || !callbackUrl) {
    return new Response(`runId, teamUrl, callbackUrl are required`, {
      status: 400,
    });
  }
  try {
    const result = await fetch(`${API_BASE_URL}/evaluate`, {
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
      console.error("Failed to get evaluation", error);
      return new Response(`Failed to get evaluation: ${error.message}`, {
        status: 500,
      });
    }
  }
}
