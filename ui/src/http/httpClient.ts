import { StorageKey } from "../constant/localStorage";

const BASE_URL = process.env.REACT_APP_API_URL ?? "https://localhost:443";

class HttpError extends Error {
  readonly status: number;

  constructor(status: number, message: string) {
    super(message);
    this.status = status;
  }
}

const request = async <P, R>({
  method,
  url,
  headers = {},
  payload,
}: {
  method: string;
  url: string;
  headers?: Record<string, string>;
  isFile?: boolean;
  payload?: P;
  file?: File | File[];
}): Promise<R> => {
  const accessToken = localStorage.getItem(StorageKey.ACCESS_TOKEN);
  const isAuth = url.includes("/api/auth");
  if (accessToken && !isAuth) {
    headers["Authorization"] = `Bearer ${accessToken}`;
  }
  let ok = false;
  let status: number = 500;
  return fetch(`${BASE_URL}${url}`, {
    mode: "cors",
    method,
    headers: {
      ...headers,
    },
    body: payload ? JSON.stringify(payload) : undefined,
  })
    .then((res) => {
      ok = res.ok;
      status = res.status;
      return res.json();
    })
    .then((json) => {
      if (ok) {
        return json;
      } else {
        throw new HttpError(status, json.message);
      }
    })
    .catch((err: Error) => {
      throw new HttpError(status, err.message);
    });
};

const get = async <R>(url: string): Promise<R> =>
  request({
    method: "GET",
    url,
  });

const post = async <P, R>(url: string, payload?: P): Promise<R> =>
  request({
    method: "POST",
    url,
    payload,
  });

const deleteFn = async <R>(url: string): Promise<R> =>
  request({ method: "DELETE", url });

const httpClient = {
  get,
  post,
  delete: deleteFn,
};

export default httpClient;
