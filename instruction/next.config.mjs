/** @type {import('next').NextConfig} */
const nextConfig = {
  rewrites: () => [
    { source: '/evaluate', destination: '/api/evaluate' },
  ]
};

export default nextConfig;
