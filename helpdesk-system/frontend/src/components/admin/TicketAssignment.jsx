import { useEffect, useState } from 'react';
import { getAgentLoad } from '../../services/assignment';

const TicketAssignment = () => {
    const [agentLoad, setAgentLoad] = useState([]);

    useEffect(() => {
        getAgentLoad().then((response) => {
            setAgentLoad(response.data);
        });
    }, []);

    return (
        <div>
            <h2>Agent Load</h2>
            <ul>
                {agentLoad.map((agent) => (
                    <li key={agent.agentUsername}>
                        {agent.agentUsername}: {agent.assignedTickets}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default TicketAssignment;
