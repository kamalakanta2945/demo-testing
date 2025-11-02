import { useEffect, useState } from 'react';
import { getAnalytics } from '../../services/analytics';

const AnalyticsChart = () => {
    const [analytics, setAnalytics] = useState([]);

    useEffect(() => {
        getAnalytics().then((response) => {
            setAnalytics(response.data);
        });
    }, []);

    return (
        <div>
            <h2>Analytics</h2>
            <ul>
                {analytics.map((metric) => (
                    <li key={metric.metricName}>
                        {metric.metricName}: {metric.metricValue}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default AnalyticsChart;
