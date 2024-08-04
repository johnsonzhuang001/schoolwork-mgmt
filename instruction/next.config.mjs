/** @type {import('next').NextConfig} */
const nextConfig = {
  rewrites: () => [
    { source: '/evaluate', destination: '/api/evaluate' },
  ],
  output: 'standalone',
};

export default nextConfig;
