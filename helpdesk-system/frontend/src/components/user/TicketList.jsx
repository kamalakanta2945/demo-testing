import { useEffect, useState } from 'react';
import { getAllTickets } from '../../services/ticket';
import { Link } from 'react-router-dom';
import { List, ListItem, ListItemText, Typography, Paper, Container, CircularProgress } from '@mui/material';

const TicketList = () => {
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getAllTickets().then((response) => {
            setTickets(response.data);
            setLoading(false);
        });
    }, []);

    if (loading) {
        return <CircularProgress />;
    }

    return (
        <Container component="main" maxWidth="md">
            <Paper elevation={3} style={{ padding: 20 }}>
                <Typography component="h1" variant="h5">
                    My Tickets
                </Typography>
                <List>
                    {tickets.map((ticket) => (
                        <ListItem button component={Link} to={`/tickets/${ticket.id}`} key={ticket.id}>
                            <ListItemText primary={ticket.title} secondary={`Status: ${ticket.status}`} />
                        </ListItem>
                    ))}
                </List>
            </Paper>
        </Container>
    );
};

export default TicketList;
