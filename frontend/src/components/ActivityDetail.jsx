import { Box, Card, CardContent, Divider, Typography } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router';
import { getActivityDetail, getActivityById } from '../services/api';

const ActivityDetail = () => {

  const { id } = useParams();
  const [activity, setActivity] = useState(null);
  const [recommendation, setRecommendation] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      // Fetch activity details first as they are the primary data
      try {
        const activityRes = await getActivityById(id);
        setActivity(activityRes.data);
      } catch (error) {
        console.error("Error fetching activity details", error);
      }

      // Fetch recommendations separately so failure doesn't block the UI
      try {
        const recommendationRes = await getActivityDetail(id);
        setRecommendation(recommendationRes.data);
      } catch (error) {
        console.warn("AI recommendation not found or service error", error);
      }
    }

    fetchData();
  }, [id]);

  if (!activity) {
    return <Typography sx={{ p: 4 }}>Loading activity details...</Typography>
  }

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', p: 2 }}>
            <Card sx={{ mb: 2 }}>
                <CardContent>
                    <Typography variant="h5" gutterBottom>Activity Details</Typography>
                    <Typography>Type: {activity.type}</Typography>
                    <Typography>Duration: {activity.duration} minutes</Typography>
                    <Typography>Calories Burned: {activity.caloriesBurned}</Typography>
                    <Typography>Date: {new Date(activity.createdAt).toLocaleString()}</Typography>
                </CardContent>
            </Card>

            {recommendation && (
                <Card>
                    <CardContent>
                        <Typography variant="h5" gutterBottom>AI Recommendation</Typography>
                        <Typography variant="h6">Analysis</Typography>
                        <Typography paragraph>{recommendation.recommendation}</Typography>
                        
                        <Divider sx={{ my: 2 }} />
                        
                        <Typography variant="h6">Improvements</Typography>
                        {recommendation.improvements?.map((improvement, index) => (
                            <Typography key={index} paragraph>• {improvement}</Typography>
                        ))}
                        
                        <Divider sx={{ my: 2 }} />
                        
                        <Typography variant="h6">Suggestions</Typography>
                        {recommendation.suggestions?.map((suggestion, index) => (
                            <Typography key={index} paragraph>• {suggestion}</Typography>
                        ))}
                        
                        <Divider sx={{ my: 2 }} />
                        
                        <Typography variant="h6">Safety Guidelines</Typography>
                        {recommendation.safety?.map((safety, index) => (
                            <Typography key={index} paragraph>• {safety}</Typography>
                        ))}
                    </CardContent>
                </Card>
            )}
        </Box>
  )
}

export default ActivityDetail