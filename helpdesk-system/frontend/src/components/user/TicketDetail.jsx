import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getTicketById, addReply, getReplies } from '../../services/ticket';
import { Button, TextField, Typography, Container, Paper, List, ListItem, ListItemText, Divider, CircularProgress } from '@mui/material';

const TicketDetail = () => {
    const { id } = useParams();
    const [ticket, setTicket] = useState(null);
    const [replies, setReplies] = useState([]);
    const [reply, setReply] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        Promise.all([getTicketById(id), getReplies(id)]).then(([ticketResponse, repliesResponse]) => {
            setTicket(ticketResponse.data);
            setReplies(repliesResponse.data);
            setLoading(false);
        });
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();
        addReply(id, { content: reply }).then(() => {
            setReply('');
            getReplies(id).then((response) => {
                setReplies(response.data);
            });
        });
    };

    if (loading) {
        return <CircularProgress />;
    }

    return (
        <Container component="main" maxWidth="md">
            <Paper elevation={3} style={{ padding: 20 }}>
                <Typography variant="h4" gutterBottom>{ticket.title}</Typography>
                <Typography variant="body1" gutterBottom>{ticket.description}</Typography>
                <Typography variant="subtitle2">Status: {ticket.status}</Typography>
                <Typography variant="subtitle2">Department: {ticket.department}</Typography>
                <Typography variant="subtitle2">Assigned to: {ticket.assignedTo || 'Unassigned'}</Typography>
            </Paper>

            <Paper elevation={3} style={{ padding: 20, marginTop: 20 }}>
                <Typography variant="h5" gutterBottom>Replies</Typography>
                <List>
                    {replies.map((r, index) => (
                        <div key={r.id}>
                            <ListItem alignItems="flex-start">
                                <ListItemText
                                    primary={r.content}
                                    secondary={`By: ${r.createdBy} at ${new Date(r.createdAt).toLocaleString()}`}
                                />
                            </ListItem>
                            {index < replies.length - 1 && <Divider />}
                        </div>
                    ))}
                </List>
            </Paper>

            <Paper elevation={3} style={{ padding: 20, marginTop: 20 }}>
                <Typography variant="h5" gutterBottom>Add a Reply</Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        name="reply"
                        label="Reply"
                        id="reply"
                        multiline
                        rows={4}
                        value={reply}
                        onChange={(e) => setReply(e.target.value)}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        style={{ marginTop: 2 }}
                    >
                        Reply
                    </Button>
                </form>
            </Paper>
        </Container>
    );
};

export default TicketDetail;
